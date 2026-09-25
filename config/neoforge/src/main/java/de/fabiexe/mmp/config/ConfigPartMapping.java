package de.fabiexe.mmp.config;

import net.neoforged.neoforge.common.ModConfigSpec;

record ConfigPartMapping<T>(ConfigPart<T> configPart, ModConfigSpec.ConfigValue<T> configValue) {
}