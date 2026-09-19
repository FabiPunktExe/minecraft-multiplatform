package de.fabiexe.mmp.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Property

interface MultiloaderLoaderExtension : MultiloaderExtension {
    val commonProject: Property<Project>
}