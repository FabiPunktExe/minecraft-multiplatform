plugins {
    id("de.fabiexe.minecraft-multiplatform.multiloader.loader")
    `java-library`
    `maven-publish`
    alias(libs.plugins.neoforge.moddev)
}

group = "de.fabiexe.minecraft-multiplatform"
version = "0.1.0"

multiloader {
    projectName = "mmp-config"
    javaVersion = 25
    commonProject = project(":config:common")
}

neoForge {
    version = libs.versions.neoforge.neoforge.get()
}

java {
    withSourcesJar()
    withJavadocJar()
}