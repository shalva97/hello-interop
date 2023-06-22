@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetHierarchyBuilder
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.KonanTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    //TODO: setup 20 toolchain only for FFM compilation - is it possible?
//    jvmToolchain(20) //for FFM

    fun KotlinTargetHierarchyBuilder.withPlatform(
        platformType: KotlinPlatformType,
        block: (KotlinTarget) -> Boolean = { true }
    ) {
        withCompilations {
            it.target.platformType == platformType &&
                    block(it.target)
        }
    }

    targetHierarchy.custom {
        common {
            group("nonNative") {
                //shares library loading mechanism
                group("jvmAndAndroid") {
                    //shared JNI declarations mechanism
                    group("jni") {
                        withAndroid()
                        withPlatform(KotlinPlatformType.jvm) { it.name.contains("jni", ignoreCase = true) }
                    }

                    //shares library loading from jar or from system
                    group("jvm") {
                        withJvm()
                    }
                }
                group("web") {
                    withJs()
                    withPlatform(KotlinPlatformType.wasm)
                }
            }
            group("native") {
                group("nativeIntBased") {
                    withIosArm32()
                }
                group("nativeLongBased") {
                    withPlatform(KotlinPlatformType.native) {
                        (it as KotlinNativeTarget).konanTarget != KonanTarget.IOS_ARM32
                    }
                }
            }
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
            }
        }
    }

    sourceSets.all {
        languageSettings {
            progressiveMode = true
            optIn("kotlin.ExperimentalStdlibApi")
        }
    }
}
