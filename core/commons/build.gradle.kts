plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)

  alias(libs.plugins.compose)
  alias(libs.plugins.compose.multiplatform)

  alias(libs.plugins.secrets)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.datetime)

      implementation(libs.compose.runtime)
      implementation(libs.compose.resources)
    }

    androidMain.dependencies {
      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.androidx.browser)
    }

    commonTest.dependencies {
      implementation(projects.core.testing)
    }
  }
}

android {
  buildFeatures {
    buildConfig = true
  }
  defaultConfig {
    buildConfigField("Integer", "VERSION_CODE", libs.versions.version.code.get())
    buildConfigField("String", "VERSION_NAME", "\"${libs.versions.version.name.get()}\"")
  }
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}

compose.resources {
  publicResClass = true
  packageOfResClass = "com.divinelink.core.commons.resources"
  generateResClass = auto
}
