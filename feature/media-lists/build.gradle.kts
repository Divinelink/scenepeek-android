plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)

  alias(libs.plugins.divinelink.compose.feature)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.domain)
      implementation(projects.core.data)
    }
  }
}

compose.resources {
  publicResClass = false
  packageOfResClass = "com.divinelink.feature.media.lists.resources"
  generateResClass = auto
}
