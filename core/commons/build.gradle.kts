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
    resValue("string", "version_name", libs.versions.version.name.get())
  }
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.timber)

  implementation(libs.kotlinx.datetime)

  testImplementation(projects.core.testing)
}
