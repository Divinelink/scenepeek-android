plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.divinelink.android.library.compose)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  implementation(libs.kotlinx.serialization.json)

  implementation(projects.core.datastore)
  implementation(projects.core.domain)
  implementation(projects.core.navigation)
  implementation(projects.core.scaffold)

  testImplementation(projects.core.testing)
}
