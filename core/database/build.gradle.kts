plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)

  alias(libs.plugins.sqldelight)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.model)
      implementation(projects.core.commons)

      api(libs.sqldelight.coroutines)
      implementation(libs.kotlinx.datetime)
    }

    androidMain.dependencies {
      implementation(libs.sqldelight.android)
    }

    nativeMain.dependencies {
      implementation(libs.sqldelight.native)
    }
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.divinelink.core.database")
      migrationOutputDirectory.set(file("src/main/sqldelight/migrations"))
      version = 7
    }
  }
}
