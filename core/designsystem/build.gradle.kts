plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

dependencies {
  api(libs.compose.ui.foundation)
  api(libs.compose.material.icons)
  api(libs.compose.material3)
  api(libs.compose.material3.adaptive)
  api(libs.compose.material3.navigationSuite)
  api(libs.compose.runtime)

  implementation(libs.androidx.core.ktx)
}
