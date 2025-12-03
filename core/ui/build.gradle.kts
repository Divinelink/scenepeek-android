plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.designsystem)
      api(projects.core.model)

      implementation(projects.core.commons)
      implementation(libs.compose.resources)

      implementation(libs.coil)
      implementation(libs.coil.ktor)

      implementation(libs.kotlinx.datetime)

      implementation(projects.core.fixtures)

      api(libs.compose.shimmer)
    }

    androidMain.dependencies {
      implementation(libs.androidx.core.ktx)
      implementation(libs.androidx.lifecycle.runtime.ktx)
      implementation(libs.androidx.browser)
      implementation(libs.ktor.client.android)
      implementation(libs.android.youtube.player)
    }

    nativeMain.dependencies {
      implementation(libs.ktor.client.darwin)
    }

    commonTest.dependencies {
      implementation(projects.core.testing)
    }
  }
}

compose.resources {
  publicResClass = true
  packageOfResClass = "com.divinelink.core.ui.resources"
  generateResClass = auto
}

android {
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  testOptions.unitTests.isIncludeAndroidResources = true
}
