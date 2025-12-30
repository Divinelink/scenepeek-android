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
          implementation(libs.koin.core)
          implementation(libs.napier)
          implementation(libs.kotlinx.serialization.json)
        }
      }

      androidMain {
        dependencies {
          implementation(libs.koin.android)
          implementation(libs.androidx.tracing.ktx)
        }
      }

      commonTest.dependencies {
        implementation(kotlin("test"))
      }
    }
  }
