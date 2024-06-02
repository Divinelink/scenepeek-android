plugins {
  alias(libs.plugins.divinelink.android.library)
}

android {
  namespace = "com.divinelink.commons"
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.timber)
}
