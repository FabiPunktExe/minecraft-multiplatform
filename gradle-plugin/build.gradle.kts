plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}

group = "de.fabiexe.minecraft-multiplatform"
version = "0.1.0"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("multiloader-common") {
            id = "$group.multiloader.common"
            implementationClass = "de.fabiexe.mmp.gradle.MultiloaderCommonPlugin"
            displayName = "Multiloader Common Plugin"
            description = "A Gradle plugin for building Minecraft mods that support multiple mod loaders (Forge, Fabric, etc.)"
        }
        create("multiloader-loader") {
            id = "$group.multiloader.loader"
            implementationClass = "de.fabiexe.mmp.gradle.MultiloaderLoaderPlugin"
            displayName = "Multiloader Loader Plugin"
            description = "A Gradle plugin for building Minecraft mods that support a specific mod loader"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}