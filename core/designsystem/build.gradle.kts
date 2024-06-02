plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.library.compose)
}

android {
  namespace = "com.divinelink.core.designsystem"
}

dependencies {
  api(libs.compose.material.icons)
  api(libs.compose.material3)
  api(libs.compose.runtime)

  implementation(libs.androidx.core.ktx)
}
