package de.fabiexe.mmp.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.named

@Suppress("unused")
class MultiloaderLoaderPlugin : MultiloaderPlugin<MultiloaderLoaderExtension>(MultiloaderLoaderExtension::class) {
    override fun apply(target: Project) {
        super.apply(target)

        val extension = target.extensions.getByName<MultiloaderLoaderExtension>("multiloader")

        val multiloaderCommonClasspath = target.configurations.create("multiloaderCommonClasspath") {
            isCanBeConsumed = false
            isCanBeResolved = true
        }
        target.configurations["compileOnly"].extendsFrom(multiloaderCommonClasspath)

        target.dependencies {
            val commonProjectDependency = extension.commonProject
                .map(Project::getPath)
                .map { path ->
                    val dependency = target.dependencies.project(path) as ModuleDependency
                    dependency.isTransitive = false
                    dependency
                }
            add("multiloaderCommonClasspath", commonProjectDependency)
        }

        target.tasks.named<Jar>("jar") {
            dependsOn(multiloaderCommonClasspath)
            from(multiloaderCommonClasspath.map { if (it.isDirectory) it else target.zipTree(it) })
        }
    }
}