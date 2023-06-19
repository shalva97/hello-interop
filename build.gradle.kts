plugins {
    kotlin("multiplatform") version "1.8.21"
    id("buildx-use-jvm-jni")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {

    macosX64() {
        binaries {
            sharedLib {
                baseName = "nativeinterop"
            }
        }
    }

    sourceSets {
        val commonMain by getting
    }
}

tasks.named<jni.BuildJni>("buildJni") {
    outputFilePath.set("macos-arm64/libffi-jni.dylib")
    includeDirs.add("build/bin/macosX64/debugShared")
}
