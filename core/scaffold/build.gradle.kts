plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.ui)
      implementation(projects.core.data)
      implementation(projects.core.domain)
      implementation(projects.core.model)
    }
  }
}

compose.resources {
  publicResClass = false
  packageOfResClass = "com.divinelink.core.scaffold.resources"
  generateResClass = auto
}
