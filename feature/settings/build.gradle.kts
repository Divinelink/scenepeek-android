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
      implementation(projects.core.datastore) // TODO REMOVE PRIOR MERGING

      implementation(libs.material.kolor)
      implementation(libs.compose.colorpicker)
      implementation(libs.kotlinx.datetime)

      implementation(projects.core.fixtures)
    }

    androidMain.dependencies {
      implementation(libs.androidx.browser)
    }
  }
}

compose.resources {
  publicResClass = true
  packageOfResClass = "com.divinelink.feature.settings.resources"
  generateResClass = auto
}
