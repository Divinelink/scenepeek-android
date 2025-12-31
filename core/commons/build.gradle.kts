plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)

  alias(libs.plugins.compose)
  alias(libs.plugins.compose.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.datetime)

      implementation(libs.compose.multiplatform.runtime)
      implementation(libs.compose.multiplatform.resources)
    }

    androidMain.dependencies {
      implementation(projects.core.android)

      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.androidx.browser)
    }
  }
}

compose.resources {
  publicResClass = true
  packageOfResClass = "com.divinelink.core.commons.resources"
  generateResClass = auto
}
