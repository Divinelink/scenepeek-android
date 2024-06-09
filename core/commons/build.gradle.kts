plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.secrets)
}

android {
  namespace = "com.divinelink.commons"

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
