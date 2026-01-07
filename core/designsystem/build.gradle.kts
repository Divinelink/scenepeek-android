plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)
}

compose.resources {
  publicResClass = false
  packageOfResClass = "com.divinelink.scenepeek.designsystem.resources"
  generateResClass = auto
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.compose.multiplatform.resources)

      implementation(libs.material.kolor)
    }

    androidMain.dependencies {
      implementation(libs.androidx.core.ktx)
    }
  }
}
