plugins {
    kotlin("jvm").version(PluginVersions.kotlin)
}

repositories {
    maven {
        setUrl("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}