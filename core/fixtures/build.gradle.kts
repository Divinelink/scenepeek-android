plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

dependencies {
  api(projects.core.model)

  implementation(libs.kotlinx.datetime)
}
