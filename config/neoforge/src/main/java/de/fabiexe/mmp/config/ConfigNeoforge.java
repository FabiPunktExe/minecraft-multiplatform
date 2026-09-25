package de.fabiexe.mmp.config;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Utility class for registering a {@link Config} with NeoForge's configuration system. */
public final class ConfigNeoforge {
    private ConfigNeoforge() {
        throw new UnsupportedOperationException();
    }

    /**
     * Registers a {@link Config} with NeoForge's configuration system.
     *
     * @param modEventBus The mod's event bus
     * @param modContainer The mod's container
     * @param config The config to register
     * @param translationKeyPrefix The translation key prefix to use for the config parts
     */
    public static void register(
            IEventBus modEventBus,
            ModContainer modContainer,
            Config config,
            String translationKeyPrefix
    ) {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        List<ConfigPartMapping<?>> configParts = new ArrayList<>();
        define(builder, config, translationKeyPrefix, List.of(), configParts);
        IConfigSpec configSpec = builder.build();
        modContainer.registerConfig(ModConfig.Type.CLIENT, configSpec);
        modEventBus.addListener(ModConfigEvent.Loading.class, new ConfigLoadListener<>(config, configSpec, configParts));
        modEventBus.addListener(ModConfigEvent.Reloading.class, new ConfigLoadListener<>(config, configSpec, configParts));
    }

    /**
     * Registers a {@link Config} with NeoForge's configuration system.
     *
     * @param modEventBus The mod's event bus
     * @param modContainer The mod's container
     * @param config The config to register
     */
    public static void register(IEventBus modEventBus, ModContainer modContainer, Config config) {
        register(modEventBus, modContainer, config, modContainer.getNamespace() + ".config.");
    }

    private static void define(
            ModConfigSpec.Builder builder,
            Config config,
            String translationKeyPrefix,
            List<String> basePath,
            List<ConfigPartMapping<?>> configParts
    ) {
        for (ConfigPart<?> part : config.parts()) {
            List<String> path = new ArrayList<>(basePath);
            path.add(part.getName());
            builder.translation(translationKeyPrefix + part.getName());
            ConfigPartMapping<?> mapping = define(builder, part, translationKeyPrefix, path, configParts);
            if (mapping != null) {
                configParts.add(mapping);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> @Nullable ConfigPartMapping<T> define(
            ModConfigSpec.Builder builder,
            ConfigPart<T> part,
            String translationKeyPrefix,
            List<String> path,
            List<ConfigPartMapping<?>> configParts
    ) {
        ModConfigSpec.ConfigValue<?> configValue = switch (part) {
            case BooleanConfigPart booleanPart -> builder.define(path, booleanPart::getDefaultValue);
            case ConfigConfigPart configPart -> {
                define(builder, configPart.getConfig(), translationKeyPrefix, path, configParts);
                yield null;
            }
            case EnumConfigPart<?> enumPart -> defineEnum(builder, path, enumPart);
        };

        if (configValue != null) {
            return new ConfigPartMapping<>(part, (ModConfigSpec.ConfigValue<T>) configValue);
        } else {
            return null;
        }
    }

    private static <T extends Enum<T>> ModConfigSpec.EnumValue<T> defineEnum(
            ModConfigSpec.Builder builder,
            List<String> path,
            EnumConfigPart<T> configPart
    ) {
        return builder.defineEnum(path, configPart.getDefaultValue(), configPart.getAllowedValues());
    }
}