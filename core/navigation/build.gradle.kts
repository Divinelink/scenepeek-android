plugins {
  alias(libs.plugins.divinelink.android.library)

  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  implementation(projects.core.model)
  implementation(libs.kotlinx.serialization.json)
}
