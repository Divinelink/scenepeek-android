/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.divinelink.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginManager.apply {
      apply("org.jetbrains.compose")
      apply("org.jetbrains.kotlin.plugin.compose")
    }

    // Android-specific configuration
    extensions.configure<com.android.build.gradle.LibraryExtension> {
      testOptions.unitTests.isIncludeAndroidResources = true
    }

    extensions.configure<KotlinMultiplatformExtension> {
      sourceSets.apply {
        all {
          languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
        }

        commonMain {
          dependencies {
            implementation(project(":core:ui"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:scaffold"))

            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.navigation)
          }
        }
      }
    }
  }
}
