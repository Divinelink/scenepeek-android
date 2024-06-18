plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.hilt)
  alias(libs.plugins.secrets)
}

android {
  buildFeatures {
    buildConfig = true
  }
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.timber)
}
