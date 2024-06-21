plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.ksp)
}

dependencies {
  api(projects.core.model)

  implementation(libs.room.ktx)
  implementation(libs.room.runtime)
  ksp(libs.room.compiler)
}
