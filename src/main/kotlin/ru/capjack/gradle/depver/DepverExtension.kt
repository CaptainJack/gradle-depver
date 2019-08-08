package ru.capjack.gradle.depver

import java.io.File

interface DepverExtension {
	var configFile: File
	
	operator fun get(dependency: String): String?
	
	operator fun get(group: String, name: String): String?
	
	operator fun set(dependency: String, version: String)
	
	operator fun set(group: String, name: String, version: String)
}
