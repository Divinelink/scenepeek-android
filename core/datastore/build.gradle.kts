plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.koin)
}

android {
  defaultConfig {
    consumerProguardFiles("consumer-proguard-rules.pro")
  }
}

dependencies {
  implementation(libs.datastore)
  implementation(libs.datastore.core)
  implementation(libs.datastore.preferences)
  implementation(libs.datastore.preferences.core)
  implementation(libs.encrypted.preferences)
  api(projects.core.datastoreProto)
  api(projects.core.model)

  implementation(projects.core.designsystem)

  implementation(projects.core.commons)

  implementation(libs.timber)

  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(projects.core.testing)
  testImplementation(libs.robolectric)
}
