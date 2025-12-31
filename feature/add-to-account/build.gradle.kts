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
    }
  }
}

compose.resources {
  publicResClass = true
  packageOfResClass = "com.divinelink.feature.add.to.account.resources"
  generateResClass = auto
}
