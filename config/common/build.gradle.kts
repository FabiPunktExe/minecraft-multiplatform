plugins {
    `java-library`
    `maven-publish`
}

group = "de.fabiexe.minecraft-multiplatform"
version = "0.1.0"

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
        artifactId = "config-common"
    }
}