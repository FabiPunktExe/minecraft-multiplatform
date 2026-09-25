package de.fabiexe.mmp.config;

import java.util.List;

final class ConfigConfigPart extends ConfigPart<List<ConfigPart<?>>> {
    private final Config config;

    ConfigConfigPart(String name, Config config) {
        super(name, cast(List.class), config.parts());
        this.config = config;
    }

    @Override
    public List<List<ConfigPart<?>>> getAllowedValues() {
        return List.of(config.parts());
    }

    public Config getConfig() {
        return config;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object value) {
        return (T) value;
    }
}