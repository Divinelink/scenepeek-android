plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

dependencies {
  api(projects.core.model)
  implementation(projects.core.data)
  implementation(projects.core.domain)

  implementation(libs.kotlinx.datetime)
}
