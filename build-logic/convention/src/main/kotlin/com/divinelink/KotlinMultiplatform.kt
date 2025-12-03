package com.divinelink

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

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

    listOf(
      iosX64(),
      iosArm64(),
      iosSimulatorArm64(),
    ).forEach {
      it.binaries.framework {
        baseName = "app"
        isStatic = true
      }
    }

    // targets
    androidTarget()
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    // common dependencies
    sourceSets.apply {
      commonMain {
        dependencies {
          implementation(libs.findLibrary("koin.core").get())
          implementation(libs.findLibrary("napier").get())
          implementation(libs.findLibrary("kotlinx.serialization.json").get())
        }
      }

      androidMain {
        dependencies {
          implementation(libs.findLibrary("koin.android").get())
          implementation(libs.findLibrary("androidx.tracing.ktx").get())
        }
      }

      commonTest.dependencies {
        implementation(kotlin("test"))
      }
    }
  }
