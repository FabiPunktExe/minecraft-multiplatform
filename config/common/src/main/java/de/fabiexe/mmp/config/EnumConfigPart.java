package de.fabiexe.mmp.config;

import java.util.List;

final class EnumConfigPart<E extends Enum<E>> extends ConfigPart<E> {
    private final Class<E> enumClass;

    EnumConfigPart(String name, Class<E> enumClass, E defaultValue) {
        super(name, enumClass, defaultValue);
        this.enumClass = enumClass;
    }

    @Override
    public List<E> getAllowedValues() {
        return List.of(enumClass.getEnumConstants());
    }
}