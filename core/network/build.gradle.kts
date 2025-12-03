plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)

  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.secrets)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.commons)
      implementation(projects.core.datastore)
      implementation(projects.core.model)

      implementation(libs.ktor.client.logging)
      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.content.negotiation)
      implementation(libs.ktor.serialization.kotlinx.json)
      implementation(libs.kotlinx.serialization.json)

      implementation(libs.kotlinx.datetime)
    }

    androidMain.dependencies {
      implementation(libs.ktor.client.android)
    }

    nativeMain.dependencies {
      implementation(libs.ktor.client.darwin)
    }

    commonTest {
      resources.srcDir("src/commonTest/resources")

      dependencies {
        implementation(kotlin("test"))
        implementation(projects.core.testing)
      }
    }
  }
}

android {
  buildTypes {
    release {
      buildConfigField(
        "String",
        "TMDB_AUTH_TOKEN",
        System.getenv("TMDB_AUTH_TOKEN") ?: "",
      )
    }
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }

  buildFeatures {
    buildConfig = true
  }
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}
