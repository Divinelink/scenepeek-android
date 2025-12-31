package com.divinelink

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

internal fun Project.configureKotlinMultiplatform(extension: KotlinMultiplatformExtension) =
  extension.apply {
    jvmToolchain(21)

    sourceSets.apply {
      all {
        languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
        languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        languageSettings.optIn("kotlin.time.ExperimentalTime")
        languageSettings.optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
      }
    }

    targets
      .withType<KotlinNativeTarget>()
      .matching { it.konanTarget.family.isAppleFamily }
      .configureEach {
        binaries {
          framework {
            baseName = "app"
            isStatic = true
          }
        }
      }

    tasks
      .withType<AbstractTestTask>()
      .configureEach {
        failOnNoDiscoveredTests.set(false)
      }

    // targets
    androidLibrary {
      val moduleName = path.split(":").drop(1).joinToString(".") { it.replace("-", ".") }
      namespace = if (moduleName.isNotEmpty()) {
        "com.divinelink.$moduleName"
      } else {
        "com.divinelink"
      }
      println("namespace: $namespace")

      minSdk = libs.versions.min.sdk.get().toInt()
      compileSdk = libs.versions.compile.sdk.get().toInt()

      androidResources { enable = true }

      withHostTest {
        isIncludeAndroidResources = true
      }
    }
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
      commonMain.dependencies {
        implementation(libs.koin.core)
        implementation(libs.napier)
        implementation(libs.kotlinx.serialization.json)
      }

      androidMain.dependencies {
        implementation(libs.koin.android)
        implementation(libs.androidx.tracing.ktx)
      }

      commonTest.dependencies {
        implementation(kotlin("test"))
        implementation(project(":core:testing"))
      }
    }
  }
