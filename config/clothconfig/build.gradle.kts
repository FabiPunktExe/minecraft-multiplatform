plugins {
    id("de.fabiexe.minecraft-multiplatform.multiloader.loader")
    `java-library`
    `maven-publish`
    alias(libs.plugins.fabric.loom)
}

group = "de.fabiexe.minecraft-multiplatform"
version = "0.1.0"

multiloader {
    projectName = "mmp-config"
    javaVersion = 25
    commonProject = project(":config:common")
}

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me")
}

dependencies {
    minecraft(libs.minecraft)
    api(libs.clothConfig)
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

publishing {
    repositories {
        maven("https://repo.diruptio.de/repository/maven-public-releases") {
            name = "diruptioPublic"
            credentials {
                username = (System.getenv("DIRUPTIO_REPO_USERNAME") ?: project.findProperty("maven_username") ?: "").toString()
                password = (System.getenv("DIRUPTIO_REPO_PASSWORD") ?: project.findProperty("maven_password") ?: "").toString()
            }
        }
    }

    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}