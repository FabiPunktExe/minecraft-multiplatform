rootProject.name = "minecraft-multiplatform"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://maven.fabricmc.net")
	}
}

includeBuild("gradle-plugin")
include(":config:clothconfig")
include(":config:common")
include(":config:neoforge")