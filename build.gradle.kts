allprojects {
    group = "com.github.protopuch.raccoon"
    version = property("version") as String

    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://raw.github.com/weddini/spring-boot-throttling/mvn-repo")
    }
}
