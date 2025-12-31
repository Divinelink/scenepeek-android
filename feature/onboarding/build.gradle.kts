plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)

  alias(libs.plugins.divinelink.compose.feature)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.commons)
      implementation(projects.core.domain)
      implementation(projects.core.datastore)

      implementation(libs.kotlinx.serialization.json)
    }
  }
}

compose.resources {
  publicResClass = false
  packageOfResClass = "com.divinelink.feature.onboarding.resources"
  generateResClass = auto
}
