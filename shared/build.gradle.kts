import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
//    val hostOs = System.getProperty("os.name")
//    val isArm64 = System.getProperty("os.arch") == "aarch64"
//    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
//        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
//        hostOs == "Linux" && isArm64 -> linuxArm64("native")
//        hostOs == "Linux" && !isArm64 -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }
//
//    nativeTarget.apply {
//        compilations.getByName("main") {
//            cinterops {
//                val libcurl by creating {
//                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
//                    packageName("com.spacecraft.kmp")
//                    compilerOpts("-I/path")
//                    includeDirs.allHeaders("path")
//                }
//            }
//        }
//        binaries {
//            executable {
//                entryPoint = "main"
//            }
//        }
//    }
    //指定main函数入口
    fun KotlinNativeTarget.config() {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64() {
        val main by compilations.getting {
            cinterops {
                val libcurl by creating {
                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
//                    packageName("com.spacecraft.kmp")
                    compilerOpts("-I/src/nativeInterop/cinterop/")
                    includeDirs.allHeaders("src/nativeInterop/cinterop/")
                }
            }
        }
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
//    mingwX64() // on Windows
//    macosX64() { // on macOS
//        binaries {
//            executable()
//        }
//    }
    androidNativeArm64()
    androidNativeArm32 {
        binaries {
            sharedLib("aa", listOf(RELEASE))
        }
//        compilations.getByName("main") {
//            cinterops {
//                val libcurl by creating {
//                    defFile(project.file("src/nativeInterop/cinterop/libcurl.def"))
//                    packageName("com.spacecraft.kmp")
//                    compilerOpts("-I/path")
//                    includeDirs.allHeaders("path")
//                }
//            }
//        }
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
        jvmMain.dependencies {

        }
        nativeMain.dependencies { }

    }

}

android {
    namespace = "org.electrolytej.f.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
tasks.withType<Wrapper> {
    gradleVersion = "8.1.1"
    distributionType = Wrapper.DistributionType.BIN
}
