plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.divinelink.android.library.compose)
}

dependencies {
  implementation(projects.core.commons)
  implementation(projects.core.domain)
  implementation(projects.core.datastore)

  implementation(libs.androidx.browser)

  implementation(projects.core.scaffold)

  implementation(projects.core.fixtures)
  testImplementation(projects.core.testing)
}
