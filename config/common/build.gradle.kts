plugins {
    id("de.fabiexe.minecraft-multiplatform.multiloader.common")
    `java-library`
    `maven-publish`
}

group = "de.fabiexe.minecraft-multiplatform"
version = "0.1.0"

multiloader {
    projectName = "mmp-config"
    javaVersion = 25
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jspecify)
}

java {
    withSourcesJar()
    withJavadocJar()
}