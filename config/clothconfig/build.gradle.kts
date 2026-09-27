plugins {
    `java-library`
    alias(libs.plugins.fabric.loom)
    `maven-publish`
}

group = "de.fabiexe.minecraft-multiplatform"
version = "0.1.0"

repositories {
    maven("https://maven.shedaniel.me")
}

dependencies {
    minecraft(libs.minecraft)
    api(projects.config.common)
    implementation(libs.clothConfig)
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
        artifactId = "config-clothconfig"
    }
}