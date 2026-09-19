package de.fabiexe.mmp.gradle

import org.gradle.api.provider.Property

interface MultiloaderExtension {
    val projectName: Property<String>
    val javaVersion: Property<Int>
}