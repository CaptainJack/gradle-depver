plugins {
	`kotlin-dsl`
	`java-gradle-plugin`
	`maven-publish`
	id("com.gradle.plugin-publish") version "0.11.0"
	id("nebula.release") version "14.1.1"
}

group = "ru.capjack.gradle"

repositories {
	jcenter()
}

dependencies {
	implementation(kotlin("gradle-plugin"))
	implementation("org.yaml:snakeyaml:1.24")
}

kotlinDslPluginOptions {
	experimentalWarning.set(false)
}

gradlePlugin {
	plugins.create("Depver") {
		id = "ru.capjack.depver"
		implementationClass = "ru.capjack.gradle.depver.DepverPlugin"
		displayName = "Depver"
	}
}

pluginBundle {
	vcsUrl = "https://github.com/CaptainJack/gradle-depver"
	website = vcsUrl
	description = "Easily manage versions of dependencies"
	tags = listOf("capjack", "manage", "dependencies", "versions")
}

tasks["postRelease"].dependsOn("publishPlugins")