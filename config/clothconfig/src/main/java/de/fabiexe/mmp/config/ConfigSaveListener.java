package de.fabiexe.mmp.config;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

record ConfigSaveListener(Config config, String modId) implements Runnable {
    @Override
    public void run() {
        JsonObject json = new JsonObject();
        save(config, json);

        Path path = Path.of("config", modId + ".json");
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, json.toString());
        } catch (IOException e) {
            ConfigClothConfig.LOGGER.error("Failed to save config to {}", path.toAbsolutePath(), e);
        }
    }

    private void save(Config config, JsonObject json) {
        for (ConfigPart<?> part : config.parts()) {
            if (part.getValue() != null) {
                switch (part) {
                    case BooleanConfigPart booleanPart -> json.addProperty(part.getName(), booleanPart.get());
                    case EnumConfigPart<?> enumPart -> json.addProperty(part.getName(), enumPart.get().name());
                    case ConfigConfigPart configPart -> {
                        JsonObject subJson = new JsonObject();
                        save(configPart.getConfig(), subJson);
                        json.add(part.getName(), subJson);
                    }
                }
            }
        }
    }
}