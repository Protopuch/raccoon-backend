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