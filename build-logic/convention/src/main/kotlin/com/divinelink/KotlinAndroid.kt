package com.divinelink

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
  commonExtension.apply {
    val moduleName = path.split(":").drop(1).joinToString(".") { it.replace("-", ".") }
    namespace = if (moduleName.isNotEmpty()) {
      "com.divinelink.$moduleName"
    } else {
      "com.divinelink"
    }
    println("namespace: $namespace")

    compileSdk = libs.findVersion("compile-sdk").get().requiredVersion.toInt()

    defaultConfig {
      minSdk = libs.findVersion("min-sdk").get().requiredVersion.toInt()
    }

    compileOptions {
      // https://developer.android.com/studio/write/java11-minimal-support-table
      sourceCompatibility = JavaVersion.VERSION_21
      targetCompatibility = JavaVersion.VERSION_21
      isCoreLibraryDesugaringEnabled = false
    }
  }
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
  extensions.configure<JavaPluginExtension> {
    // Up to Java 11 APIs are available through desugaring
    // https://developer.android.com/studio/write/java11-minimal-support-table
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Configure base Kotlin options
 */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
  // Treat all Kotlin warnings as errors (disabled by default)
  // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
  val warningsAsErrors: String? by project
  when (this) {
    is KotlinAndroidProjectExtension -> compilerOptions
    is KotlinJvmProjectExtension -> compilerOptions
    else -> TODO("Unsupported project extension $this ${T::class}")
  }.apply {
    jvmTarget = JvmTarget.JVM_17
    allWarningsAsErrors = warningsAsErrors.toBoolean()
    freeCompilerArgs.addAll(
      // Enable experimental coroutines APIs, including Flow
      "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
      "-opt-in=kotlin.time.ExperimentalTime",
      "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
    )
  }
}
