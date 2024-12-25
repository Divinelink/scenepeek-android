plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

android {
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

dependencies {
  api(projects.core.designsystem)
  api(projects.core.model)

  implementation(projects.core.commons)

  implementation(libs.coil)
  implementation(libs.coil.ktor)

  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.youtube.player)

  implementation(projects.core.fixtures)
  testImplementation(projects.core.testing)
}
