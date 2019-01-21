import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version PluginVersions.springBoot
    id("io.spring.dependency-management") version PluginVersions.springDependencyManagement
}

val bootJar: BootJar by tasks
bootJar.apply {
    baseName = "gs-spring-boot"
    version = "0.1.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val springVersion = PluginVersions.springBoot
val springCloudVersion: String by project.ext
val eurekaServerVersion: String by project.ext

dependencies {
    testCompile("junit", "junit", "4.12")
    compile("org.springframework.boot:spring-boot-starter")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile(project(":api"))
    compile(project(":arithmetic-engine"))
}
