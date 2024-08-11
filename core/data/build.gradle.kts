plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.hilt)
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

  implementation(projects.core.commons)

  implementation(libs.kotlinx.datetime)

  testImplementation(libs.kotlinx.serialization.json)
  testImplementation(projects.core.testing)
}
