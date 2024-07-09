plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

dependencies {
  api(projects.core.data)
  api(projects.core.model)

  api(libs.compose.ui.test.junit4)
  api(libs.kotlin.test.junit)
  api(libs.kotlinx.coroutines.test)
  api(libs.truth)
  api(libs.turbine)

  debugApi(libs.compose.ui.test.manifest)

  implementation(projects.core.ui)
  implementation(projects.core.designsystem)

  implementation(projects.core.commons)
  implementation(projects.core.domain)
  implementation(projects.core.datastore)

  implementation(libs.compose.destinations.core)

  implementation(libs.junit)
  implementation(libs.mockito)
  implementation(libs.mockito.kotlin)

  implementation(libs.androidx.compose.ui.test)
  implementation(libs.robolectric)

  implementation(libs.sqldelight.driver)
}
