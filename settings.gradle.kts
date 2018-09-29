rootProject.name = "raccoon-backend"

include(":backend")
include(":arithmetic-engine")

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        maven {
            setUrl("https://dl.bintray.com/kotlin/kotlin-eap")
        }
    }
}
