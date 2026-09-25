package de.fabiexe.mmp.config;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A single named configuration part with a default value.
 *
 * @param <T> The type of the configuration value
 */
public abstract sealed class ConfigPart<T> implements Supplier<T> permits BooleanConfigPart, ConfigConfigPart, EnumConfigPart {
    private final String name;
    private final Class<T> type;
    private final T defaultValue;
    private @Nullable T value = null;

    /**
     * Creates a new configuration part
     *
     * @param name The name under which the value is stored in the configuration file
     * @param type The type of the configuration value
     * @param defaultValue The default value that applies as long as no value differing from the default has been set
     */
    ConfigPart(String name, Class<T> type, T defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the list of allowed values for this configuration part.
     * If the list is empty, all values of the type are allowed.
     *
     * @return The list of allowed values
     */
    public abstract List<T> getAllowedValues();

    /**
     * Returns the name of the configuration value.
     *
     * @return The name of the configuration value
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the configuration value.
     *
     * @return The type of the configuration value
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Returns the default value.
     *
     * @return The default value
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the value, or {@code null} if no value differing from the default has been set.
     *
     * @return The value or {@code null}
     */
    public @Nullable T getValue() {
        return value;
    }

    /**
     * Returns the currently effective value: the value, or the default value if none is set.
     *
     * @return The value or the default value
     */
    @Override
    public T get() {
        return Objects.requireNonNullElse(value, defaultValue);
    }

    /**
     * Sets a new value.
     * If the value equals the default value, the set value is cleared, so the default value applies again.
     *
     * @param value The new value (may be {@code null} to clear the value)
     */
    public void set(@Nullable T value) {
        if (Objects.equals(value, defaultValue)) {
            this.value = null;
        } else {
            this.value = value;
        }
    }

    /**
     * Creates a new configuration part for a boolean value.
     *
     * @param name The name under which the value is stored in the configuration file
     * @param defaultValue The default value that applies as long as no value differing from the default has been set
     * @return The new configuration part
     */
    public static ConfigPart<Boolean> ofBoolean(String name, boolean defaultValue) {
        return new BooleanConfigPart(name, defaultValue);
    }

    /**
     * Creates a new configuration part for an enum value.
     *
     * @param name The name under which the value is stored in the configuration file
     * @param enumClass The class of the enum
     * @param defaultValue The default value that applies as long as no value differing from the default has been set
     * @return The new configuration part
     */
    public static <E extends Enum<E>> ConfigPart<E> ofEnum(String name, Class<E> enumClass, E defaultValue) {
        return new EnumConfigPart<>(name, enumClass, defaultValue);
    }

    /**
     * Creates a new configuration part for a sub-configuration.
     *
     * @param name The name under which the value is stored in the configuration file
     * @param config The sub-configuration
     * @return The new configuration part
     */
    public static ConfigPart<List<ConfigPart<?>>> ofConfig(String name, Config config) {
        return new ConfigConfigPart(name, config);
    }
}