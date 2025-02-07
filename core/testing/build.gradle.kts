plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

dependencies {
  api(projects.core.data)
  api(projects.core.model)
  api(projects.core.fixtures)

  api(libs.compose.ui.test.junit4)
  api(libs.kotlin.test.junit)
  api(libs.kotlinx.coroutines.test)
  api(libs.truth)
  api(libs.turbine)

  api(libs.androidx.navigation.testing)

  debugApi(libs.compose.ui.test.manifest)

  implementation(projects.core.ui)
  implementation(projects.core.designsystem)

  implementation(projects.core.commons)
  implementation(projects.core.domain)
  implementation(projects.core.datastore)

  implementation(libs.junit)
  implementation(libs.mockito)
  implementation(libs.mockito.kotlin)

  implementation(libs.androidx.compose.ui.test)
  implementation(libs.robolectric)

  implementation(libs.sqldelight.driver)

  implementation(libs.kotlinx.datetime)

  api(libs.ktor.client.mock)

  api(libs.koin.test)

  api(libs.datastore)
  api(libs.datastore.core)
  api(libs.datastore.preferences)
  api(libs.datastore.preferences.core)
  api(libs.encrypted.preferences)
}
