plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)

  alias(libs.plugins.divinelink.compose.feature)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.data)
      implementation(projects.core.domain)
      implementation(projects.core.fixtures)

      implementation(projects.feature.addToAccount)
      implementation(projects.feature.requestMedia)

      implementation(libs.kotlinx.datetime)
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
  publicResClass = false
  packageOfResClass = "com.divinelink.feature.details.resources"
  generateResClass = auto
}
