plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.divinelink.android.library.compose)
}

dependencies {
  implementation(projects.core.commons)
  implementation(projects.core.data)
  implementation(projects.core.datastore)
  implementation(projects.core.domain)
  implementation(projects.core.model)

  implementation(projects.core.scaffold)

  implementation(libs.androidx.browser)

  implementation(projects.core.fixtures)
  testImplementation(projects.core.testing)
}
