pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
    includeBuild("build-kotlin")
}

rootProject.name = "hello-interop"
include("jvm")
