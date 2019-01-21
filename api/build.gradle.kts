plugins {
    java
    id ("io.spring.dependency-management") version PluginVersions.springDependencyManagement
}

dependencies {
    testCompile("junit", "junit", "4.12")
    compile("org.springframework.boot:spring-boot-starter")
    testCompile("org.springframework.boot:spring-boot-starter-test")
    compile("org.springframework.boot:spring-boot-starter-web")
}

dependencyManagement {
    imports { mavenBom("org.springframework.boot:spring-boot-dependencies:${PluginVersions.springBoot}") }
}