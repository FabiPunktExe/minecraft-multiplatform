package de.fabiexe.mmp.config;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.function.Consumer;

record ConfigLoadListener<Event extends ModConfigEvent>(
        Config config,
        IConfigSpec configSpec,
        List<ConfigPartMapping<?>> configParts
) implements Consumer<Event> {
    @Override
    public void accept(Event event) {
        if (event.getConfig().getSpec() == configSpec) {
            configParts.forEach(this::load);
        }
    }

    private <T> void load(ConfigPartMapping<T> configPartMapping) {
        configPartMapping.configPart().set(configPartMapping.configValue().getRaw());
    }
}