package de.fabiexe.mmp.config;

import java.util.List;

/**
 * Represents a configuration consisting of multiple configuration parts.
 *
 * @param parts The list of configuration parts
 */
public record Config(List<ConfigPart<?>> parts) {
}