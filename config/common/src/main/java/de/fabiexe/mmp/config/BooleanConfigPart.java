package de.fabiexe.mmp.config;

import java.util.List;

/** A configuration part that holds a boolean value. */
final class BooleanConfigPart extends ConfigPart<Boolean> {
    BooleanConfigPart(String name, boolean defaultValue) {
        super(name, Boolean.class, defaultValue);
    }

    @Override
    public List<Boolean> getAllowedValues() {
        return List.of(true, false);
    }
}