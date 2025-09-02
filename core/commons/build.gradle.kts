plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.koin)
  alias(libs.plugins.secrets)
}

android {
  buildFeatures {
    buildConfig = true
  }
  defaultConfig {
    buildConfigField("Integer", "VERSION_CODE", libs.versions.version.code.get())
  }
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.timber)

  implementation(libs.kotlinx.datetime)
  implementation(libs.androidx.browser)

  testImplementation(projects.core.testing)
}
