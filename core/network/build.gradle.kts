plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.kotlin.serialization)

  // Needed for hilt
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)

  alias(libs.plugins.secrets)
}

android {
  namespace = "com.andreolas.core.network"

  buildFeatures {
    buildConfig = true
  }
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
  implementation(projects.coreUtil)
  implementation(projects.core.model)

  implementation(libs.dagger.hilt.android)
  ksp(libs.dagger.hilt.compiler)

  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.timber)
}
