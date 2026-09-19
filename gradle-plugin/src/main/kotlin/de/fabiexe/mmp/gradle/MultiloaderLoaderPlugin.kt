package de.fabiexe.mmp.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByName

class MultiloaderLoaderPlugin : MultiloaderPlugin<MultiloaderLoaderExtension>(MultiloaderLoaderExtension::class) {
    override fun apply(target: Project) {
        super.apply(target)

        target.afterEvaluate {
            val extension = extensions.getByName<MultiloaderLoaderExtension>("multiloader")

            extensions.configure<SourceSetContainer>("sourceSets") {
                for (sourceSet in this) {
                    val commonSourceSets = extension.commonProject.get().extensions.findByName("sourceSets")
                    if (commonSourceSets is SourceSetContainer) {
                        val commonSourceSet = commonSourceSets.getByName(sourceSet.name)
                        commonSourceSet.java.srcDirs.forEach(sourceSet.java::srcDir)
                        commonSourceSet.resources.srcDirs.forEach(sourceSet.resources::srcDir)
                    }
                }
            }
        }
    }
}