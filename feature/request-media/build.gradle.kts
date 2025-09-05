plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.divinelink.android.library.compose)
  alias(libs.plugins.ksp)
}

dependencies {
  implementation(projects.core.commons)
  implementation(projects.core.data)
  implementation(projects.core.domain)
  implementation(projects.core.model)

  implementation(projects.core.scaffold)

  implementation(projects.core.fixtures)
  testImplementation(projects.core.testing)
}
