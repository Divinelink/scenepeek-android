plugins {
  alias(libs.plugins.divinelink.android.library)

  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  implementation(projects.core.model)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.androidx.navigation.runtime.ktx)
  implementation(libs.androidx.navigation.compose)
}
