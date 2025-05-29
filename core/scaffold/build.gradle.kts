plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)

  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  implementation(projects.core.ui)
  implementation(projects.core.data)
  implementation(projects.core.domain)

  // Make sure this is necessary
  implementation(projects.core.navigation)

  // Navigation
  implementation(libs.androidx.navigation.runtime.ktx)
  implementation(libs.androidx.navigation.compose)

  implementation(projects.core.model)
  implementation(libs.kotlinx.serialization.json)
}
