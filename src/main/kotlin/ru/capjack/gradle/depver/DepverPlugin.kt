package ru.capjack.gradle.depver

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalDependency
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.yaml.snakeyaml.Yaml

@Suppress("UnstableApiUsage")
class DepverPlugin : Plugin<Project> {
	
	override fun apply(project: Project) {
		project.extensions.create(DepverExtension::class, "depver", DepverExtensionImpl::class, project)
		project.afterEvaluate(::init)
		project.childProjects.values.forEach {
			it.pluginManager.apply(DepverPlugin::class)
		}
	}
	
	private fun init(project: Project) {
		val extension = project.extensions.getByType<DepverExtension>()
		
		loadConfig(extension)
		
		project.configurations.all {
			withDependencies {
				forEach { dependency ->
					if (dependency is ExternalDependency && dependency.version.isNullOrEmpty()) {
						extension[dependency.group, dependency.name]?.also { version ->
							dependency.version { require(version) }
						}
					}
				}
			}
		}
	}
	
	private fun loadConfig(extension: DepverExtension) {
		if (extension.configFile.exists()) {
			val yaml = Yaml().load<Any?>(extension.configFile.bufferedReader())
			
			(yaml as? Map<*, *>)?.also { config ->
				
				for ((key, value) in config) {
					if (value is Map<*, *>) {
						for ((name, version) in value) {
							extension[key.toString(), name.toString()] = version.toString()
						}
					}
					else {
						extension[key.toString()] = value.toString()
					}
				}
			}
		}
	}
}
