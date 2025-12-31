plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.model)
      api(projects.core.network)
      api(projects.core.database)
      api(projects.core.datastore)

      implementation(projects.core.commons)

      implementation(libs.kotlinx.datetime)
    }
  }
}
