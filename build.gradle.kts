allprojects {
    group = "com.github.protopuch.raccoon"
    version = property("version") as String

    repositories {
        mavenCentral()
        jcenter()
    }
}
