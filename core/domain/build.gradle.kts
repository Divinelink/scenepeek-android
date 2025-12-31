plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.commons)
      implementation(projects.core.data)
      implementation(projects.core.database)
      implementation(projects.core.datastore)

      implementation(libs.napier)
      implementation(libs.kotlinx.datetime)
    }
  }
}
