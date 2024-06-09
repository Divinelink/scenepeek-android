plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

android {
  namespace = "com.divinelink.ui"
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

dependencies {
  api(projects.core.designsystem)
  api(projects.core.model)

  implementation(projects.core.commons)

  implementation(libs.compose.coil)
}
