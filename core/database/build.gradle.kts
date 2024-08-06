import app.cash.sqldelight.core.capitalize
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.divinelink.android.hilt)

  alias(libs.plugins.sqldelight)
}

android {
  namespace = "com.divinelink.core.database"
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

/**
 * There an issue with dagger and SQLDelight, the following is a workaround to fix it
 * https://github.com/google/dagger/issues/4158#issuecomment-1825440083
 */
androidComponents {
  onVariants(selector().all()) { variant ->
    afterEvaluate {
      val capName = variant.name.capitalize()
      tasks.getByName<KotlinCompile>("ksp${capName}Kotlin") {
        setSource(tasks.getByName("generate${capName}DatabaseInterface").outputs)
      }
    }
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName.set("com.divinelink.core.database")
      migrationOutputDirectory.set(file("src/main/sqldelight/migrations"))
      version = 3
    }
  }
}
