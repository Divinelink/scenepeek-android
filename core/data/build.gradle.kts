plugins {
  alias(libs.plugins.divinelink.android.library)

  // Needed for hilt
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.divinelink.data"
}

dependencies {
  api(projects.core.model)
  api(projects.core.network)
  api(projects.core.database)

  implementation(projects.coreUtil)

  implementation(libs.dagger.hilt.android)
  ksp(libs.dagger.hilt.compiler)
}
