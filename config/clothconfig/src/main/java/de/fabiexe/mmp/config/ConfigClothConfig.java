package de.fabiexe.mmp.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/** A utility class for integrating a {@link Config} with Cloth Config. */
public final class ConfigClothConfig {
    static final Logger LOGGER = LoggerFactory.getLogger("Minecraft Multiplatform Config");

    private ConfigClothConfig() {
        throw new UnsupportedOperationException();
    }

    /**
     * Loads a config from the given file.
     *
     * @param config The config to load
     * @param modId The mod ID to use for the translation key prefix
     */
    public static void load(Config config, String modId) {
        Path path = Paths.get("config", modId + ".json");
        try {
            JsonObject json = JsonParser.parseReader(Files.newBufferedReader(path)).getAsJsonObject();
            load(config, json);
        } catch (IOException e) {
            LOGGER.error("Failed to load config from {}", path, e);
        }
    }

    private static void load(Config config, JsonObject json) {
        for (ConfigPart<?> part : config.parts()) {
            JsonElement element = json.get(part.getName());
            if (element != null) {
                switch (part) {
                    case BooleanConfigPart booleanPart -> {
                        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
                            booleanPart.set(element.getAsBoolean());
                        }
                    }
                    case EnumConfigPart<?> enumPart -> {
                        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                            load(enumPart, element.getAsString());
                        }
                    }
                    case ConfigConfigPart configPart -> {
                        if (element.isJsonObject()) {
                            load(configPart.getConfig(), json.getAsJsonObject(part.getName()));
                        }
                    }
                }
            }
        }
    }

    private static <E extends Enum<E>> void load(EnumConfigPart<E> part, String name) {
        for (E constant : part.getType().getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(name)) {
                part.set(constant);
                break;
            }
        }
    }

    /**
     * Builds a complete Cloth Config screen for the given config.
     *
     * @param parent The screen to return to when closing
     * @param config The config to map
     * @param modId The mod ID to use for the translation key prefix
     * @param translationKeyPrefix The prefix to use for the translation keys
     * @return The new screen
     */
    public static Screen createScreen(Screen parent, Config config, String modId, String translationKeyPrefix) {
        ConfigBuilder builder = ConfigBuilder.create();
        builder.setParentScreen(parent);
        builder.setTitle(Component.translatable("text.autoconfig." + modId + ".title"));
        builder.setSavingRunnable(new ConfigSaveListener(config, modId));
        ConfigCategory category = builder.getOrCreateCategory(Component.empty());
        addEntries(config, translationKeyPrefix, builder, null, category::addEntry);
        return builder.build();
    }

    /**
     * Builds a complete Cloth Config screen for the given config.
     *
     * @param parent The screen to return to when closing
     * @param config The config to map
     * @param modId The mod ID to use for the translation key prefix
     * @return The new screen
     */
    public static Screen createScreen(Screen parent, Config config, String modId) {
        return createScreen(parent, config, modId, modId + ".config.");
    }

    private static void addEntries(
            Config config,
            String translationKeyPrefix,
            ConfigBuilder builder,
            @Nullable String path,
            Consumer<AbstractConfigListEntry<?>> entryConsumer
    ) {
        for (ConfigPart<?> part : config.parts()) {
            String key = (path != null ? path + "." : "") + part.getName();
            AbstractConfigListEntry<?> entry = switch (part) {
                case BooleanConfigPart booleanPart -> builder.entryBuilder()
                        .startBooleanToggle(Component.translatable(translationKeyPrefix + key), booleanPart.get())
                        .setDefaultValue(booleanPart::getDefaultValue)
                        .setSaveConsumer(booleanPart::set)
                        .build();
                case EnumConfigPart<?> enumPart -> buildEnumEntry(
                        builder.entryBuilder(),
                        enumPart,
                        Component.translatable(translationKeyPrefix + key)
                );
                case ConfigConfigPart configPart -> {
                    SubCategoryBuilder subCategoryBuilder = builder.entryBuilder()
                            .startSubCategory(Component.translatable(translationKeyPrefix + "category." + key));
                    addEntries(configPart.getConfig(), translationKeyPrefix, builder, key, subCategoryBuilder::add);
                    yield subCategoryBuilder.build();
                }
            };
            entryConsumer.accept(entry);
        }
    }

    private static <E extends Enum<E>> AbstractConfigListEntry<?> buildEnumEntry(
            ConfigEntryBuilder entryBuilder,
            EnumConfigPart<E> part,
            Component title
    ) {
        return entryBuilder
                .startEnumSelector(title, part.getType(), part.get())
                .setDefaultValue(part::getDefaultValue)
                .setSaveConsumer(part::set)
                .build();
    }
}