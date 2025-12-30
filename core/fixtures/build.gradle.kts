plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.model)
      implementation(projects.core.data)
      implementation(projects.core.domain)

      implementation(libs.kotlinx.datetime)
    }
  }
}
