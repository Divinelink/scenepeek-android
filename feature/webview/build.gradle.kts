plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)

  alias(libs.plugins.divinelink.compose.feature)
}

dependencies {
  implementation(projects.core.commons)
  implementation(projects.core.domain)

  implementation(projects.core.scaffold)

  implementation(projects.core.fixtures)
  testImplementation(projects.core.testing)
}
