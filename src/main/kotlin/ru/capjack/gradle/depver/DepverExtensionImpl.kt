package ru.capjack.gradle.depver

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

@Suppress("UnstableApiUsage")
open class DepverExtensionImpl(private val project: Project) : DepverExtension {
	private val versions = mutableMapOf<String, String>()
	
	private val parent: DepverExtension?
		get() = project.parent?.extensions?.findByType()
	
	override var configFile = project.file("gradle-depver.yml")
	
	override fun get(dependency: String): String? {
		val version = versions[dependency] ?: parent?.let { it[dependency] }
		
		if (version == null) {
			for ((d, v) in versions) {
				if (d.endsWith('*') && dependency.startsWith(d.substring(0, d.lastIndex))) {
					return v
				}
			}
		}
		
		return version
	}
	
	override fun get(group: String, name: String): String? {
		return get("$group:$name") ?: get(group)
	}
	
	override fun set(dependency: String, version: String) {
		versions[dependency] = version
	}
	
	override fun set(group: String, name: String, version: String) {
		set("$group:$name", version)
	}
}
