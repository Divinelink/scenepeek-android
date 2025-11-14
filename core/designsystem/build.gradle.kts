plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)
}

// dependencies {
//  api(libs.compose.ui.foundation)
//  api(libs.compose.material.icons)
//  api(libs.compose.material3)
//  api(libs.compose.material3.adaptive)
//  api(libs.compose.material3.navigationSuite)
//  api(libs.compose.runtime)
//
//  implementation(libs.androidx.core.ktx)
// }

compose.resources {
  publicResClass = false
  packageOfResClass = "com.divinelink.scenepeek.designsystem"
  generateResClass = auto
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(compose.components.resources)
    }

    androidMain.dependencies {
      implementation(libs.androidx.core.ktx)
    }
  }
}
