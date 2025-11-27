plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)

  alias(libs.plugins.divinelink.compose.feature)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.commons)
      implementation(projects.core.domain)

      implementation(libs.kotlinx.datetime)

      implementation(projects.core.fixtures)
    }

    androidMain.dependencies {
      implementation(libs.androidx.browser)
    }

    commonTest.dependencies {
      implementation(projects.core.testing)
    }
  }
}

compose.resources {
  publicResClass = true
  packageOfResClass = "com.divinelink.feature.settings"
  generateResClass = auto
}
