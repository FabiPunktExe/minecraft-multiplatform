package de.fabiexe.mmp.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType
import kotlin.reflect.KClass

open class MultiloaderPlugin<T : MultiloaderExtension>(private val extensionClass: KClass<T>) : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("java-library")

        val extension = target.extensions.create("multiloader", extensionClass)

        target.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(extension.javaVersion.map(JavaLanguageVersion::of))
        }

        target.tasks.withType<JavaCompile>().configureEach {
            options.release.set(extension.javaVersion)
            options.encoding = "UTF-8"
        }

        target.tasks.withType<Jar>().configureEach {
            if (name != "remapJar") {
                val suffix = when (name) {
                    "sourcesJar" -> "-sources"
                    "javadocJar" -> "-javadoc"
                    else -> ""
                }
                archiveFileName.set(extension.projectName.map { jarName ->
                    "$jarName-${project.version}-${project.name}$suffix.jar"
                })
            }
        }
    }
}