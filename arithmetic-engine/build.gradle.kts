import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version(PluginVersions.kotlin)
}

repositories {
    maven {
        setUrl("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

tasks.withType(KotlinCompile::class.java) {
    kotlinOptions.jvmTarget = "1.8"
}