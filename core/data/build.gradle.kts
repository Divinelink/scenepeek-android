plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.hilt)
}

android {
  namespace = "com.divinelink.data"
}

dependencies {
  api(projects.core.model)
  api(projects.core.network)
  api(projects.core.database)

  implementation(projects.core.commons)

  testImplementation(projects.core.testing)
}
