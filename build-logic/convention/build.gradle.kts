import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  `kotlin-dsl`
}

group = "com.divinelink.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_21
  }
}

dependencies {
  compileOnly(libs.android.gradle.plugin)
  compileOnly(libs.android.tools.common)
  compileOnly(libs.compose.gradle.plugin)
  compileOnly(libs.compose.multiplatform.gradle.plugin)
  compileOnly(libs.kotlin.gradle.plugin)
}

tasks {
  validatePlugins {
    enableStricterValidation = true
    failOnWarning = true
  }
}

gradlePlugin {
  plugins {
    register("kotlinMultiplatformApplication") {
      id = "com.divinelink.kotlin.multiplatform.application"
      implementationClass = "KotlinMultiplatformApplicationPlugin"
    }
    register("kotlinMultiplatform") {
      id = "com.divinelink.kotlin.multiplatform"
      implementationClass = "KotlinMultiplatformPlugin"
    }
    register("composeMultiplatform") {
      id = "com.divinelink.compose.multiplatform"
      implementationClass = "ComposeMultiplatformPlugin"
    }
    register("composeFeature") {
      id = "com.divinelink.compose.feature"
      implementationClass = "ComposeFeatureConventionPlugin"
    }
    register("androidApplication") {
      id = "divinelink.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidApplicationCompose") {
      id = "divinelink.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
//    register("androidLibrary") {
//      id = "divinelink.android.library"
//      implementationClass = "AndroidLibraryConventionPlugin"
//    }
//    register("androidFeature") {
//      id = "divinelink.android.feature"
//      implementationClass = "AndroidFeatureConventionPlugin"
//    }
    register("androidLibraryCompose") {
      id = "divinelink.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("androidKoin") {
      id = "divinelink.android.koin"
      implementationClass = "AndroidKoinConventionPlugin"
    }
  }
}
