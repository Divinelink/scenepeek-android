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

      implementation(projects.core.fixtures)
    }

    android
    commonTest.dependencies {
      implementation(projects.core.testing)
    }
  }
}
