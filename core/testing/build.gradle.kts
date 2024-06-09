plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

android {
  namespace = "com.divinelink.core.testing"
}

dependencies {
  api(projects.core.data)
  api(projects.core.model)

  implementation(projects.core.ui)
  implementation(projects.core.designsystem)

  implementation(libs.junit)
  implementation(libs.mockito)
  implementation(libs.mockito.kotlin)
  implementation(libs.kotlinx.coroutines.test)
  implementation(libs.truth)

  implementation(libs.androidx.compose.ui.test)
  implementation(libs.robolectric)
}
