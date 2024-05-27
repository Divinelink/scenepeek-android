plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.divinelink.database"
}

dependencies {
  api(projects.core.model)

  implementation(libs.room.ktx)
  implementation(libs.room.runtime)
  ksp(libs.room.compiler)
}
