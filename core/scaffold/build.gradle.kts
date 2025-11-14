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

      // Navigation
//      implementation(libs.androidx.navigation.runtime.ktx)
//      implementation(libs.androidx.navigation.compose)
    }
  }
}

compose.resources {
  publicResClass = false
  packageOfResClass = "com.divinelink.core.scaffold"
  generateResClass = auto
}

dependencies {
//  // Navigation
//  implementation(libs.androidx.navigation.runtime.ktx)
//  implementation(libs.androidx.navigation.compose)
//
//  implementation(libs.kotlinx.serialization.json)
}
