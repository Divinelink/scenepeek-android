plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.secrets)
}

android {
  buildFeatures {
    buildConfig = true
  }

  buildTypes {
    defaultConfig {
      buildConfigField("Integer", "VERSION_CODE", libs.versions.version.code.get())
      buildConfigField("String", "VERSION_NAME", "\"${libs.versions.version.name.get()}\"")
    }
  }
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}
