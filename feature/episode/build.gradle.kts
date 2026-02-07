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

      implementation(projects.feature.addToAccount)

      implementation(libs.kotlinx.datetime)
    }
  }
}

compose.resources {
  publicResClass = false
  packageOfResClass = "com.divinelink.feature.episode.resources"
  generateResClass = auto
}
