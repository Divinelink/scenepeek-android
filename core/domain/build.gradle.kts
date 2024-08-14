plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.koin)
}

dependencies {
  api(projects.core.commons)
  implementation(projects.core.data)
  implementation(projects.core.database)
  implementation(projects.core.datastore)

  implementation(libs.timber)

  testImplementation(projects.core.testing)
}
