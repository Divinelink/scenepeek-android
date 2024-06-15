plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.hilt)
}

android {
  namespace = "com.divinelink.core.domain"
}

dependencies {
  implementation(projects.core.commons)
  implementation(projects.core.data)
  implementation(projects.core.database)
  implementation(projects.core.datastore)

  implementation(libs.timber)
}
