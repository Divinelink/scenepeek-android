package com.divinelink

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
  commonExtension.apply {
    buildFeatures {
      compose = true
    }

    defaultConfig {
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    dependencies {
      val bom = libs.compose.bom
      add("implementation", platform(bom))
      add("testImplementation", platform(bom))
      add("implementation", libs.compose.ui.tooling.preview)
      add("debugImplementation", libs.compose.ui.tooling)
      add("implementation", libs.compose.shimmer)
    }

    testOptions.unitTests.isIncludeAndroidResources = true
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
  }

  extensions.configure<ComposeCompilerGradlePluginExtension> {
    fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
    fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
      rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
    }.map { it.dir(dir) }

    project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
      .relativeToRootProject("compose-metrics")
      .let(metricsDestination::set)

    project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
      .relativeToRootProject("compose-reports")
      .let(reportsDestination::set)

//    stabilityConfigurationFile = rootProject.layout.projectDirectory.file(
//      "compose_compiler_config.conf"
//    ) TODO Enable this in the future

    enableStrongSkippingMode = true
  }
}
