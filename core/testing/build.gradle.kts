plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}


dependencies {
  api(projects.core.data)
  api(projects.core.model)

  api(libs.kotlin.test.junit)
  api(libs.kotlinx.coroutines.test)
  api(libs.truth)
  api(libs.turbine)

  implementation(projects.core.ui)
  implementation(projects.core.designsystem)

  implementation(libs.junit)
  implementation(libs.mockito)
  implementation(libs.mockito.kotlin)


  implementation(libs.androidx.compose.ui.test)
  implementation(libs.robolectric)
}
