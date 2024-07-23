plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  testImplementation(projects.core.testing)

  implementation(libs.kotlinx.serialization.json)
}
