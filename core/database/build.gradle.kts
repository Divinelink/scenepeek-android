plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.koin)

  alias(libs.plugins.ksp)
  alias(libs.plugins.sqldelight)
}

dependencies {
  api(projects.core.model)
  implementation(projects.core.commons)

  implementation(libs.room.ktx)
  implementation(libs.room.runtime)
  ksp(libs.room.compiler)

  api(libs.sqldelight.android)
  api(libs.sqldelight.coroutines)

  implementation(libs.kotlinx.datetime)

  testImplementation(projects.core.testing)
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.divinelink.core.database")
      migrationOutputDirectory.set(file("src/main/sqldelight/migrations"))
      version = 4
    }
  }
}
