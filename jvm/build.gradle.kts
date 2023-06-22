plugins {
    kotlin("jvm")
//    id("buildx-use-jvm-jni")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
}
