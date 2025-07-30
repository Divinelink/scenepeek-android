plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  implementation(projects.core.commons)
  testImplementation(projects.core.testing)

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.io.core)
}
