plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.koin)
}

android {
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}

dependencies {
  api(projects.core.model)
  api(projects.core.network)
  api(projects.core.database)
  api(projects.core.datastore)

  implementation(projects.core.commons)

  implementation(libs.kotlinx.datetime)

  testImplementation(libs.kotlinx.serialization.json)
  testImplementation(projects.core.testing)
}
