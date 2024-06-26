plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.hilt)

  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.secrets)
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

  buildFeatures {
    buildConfig = true
  }
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
  implementation(projects.core.commons)
  implementation(projects.core.datastore)
  implementation(projects.core.model)

  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.timber)
}
