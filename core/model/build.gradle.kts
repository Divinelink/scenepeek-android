plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.commons)

      implementation(libs.kotlinx.datetime)

      implementation(libs.compose.resources)

      implementation(projects.core.commons)

      implementation(libs.kotlinx.serialization.json)
      implementation(libs.kotlinx.datetime)
      implementation(libs.kotlinx.io.core)
    }

    commonTest.dependencies {
      implementation(projects.core.testing)
    }
  }
}

compose.resources {
  publicResClass = true
  packageOfResClass = "com.divinelink.core.model.resources"
  generateResClass = auto
}
