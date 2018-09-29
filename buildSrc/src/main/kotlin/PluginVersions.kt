// Gradle Kotlin DSL doesn't allow using build properties to declare plugin versions.
// So we have to declare them here.
object PluginVersions {
    val kotlin = "1.3.0-rc-116"
    val springBoot = "2.0.3.RELEASE"
    val springDependencyManagement = "1.0.6.RELEASE"
}