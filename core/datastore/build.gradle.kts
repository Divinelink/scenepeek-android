plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.model)
      api(projects.core.designsystem)
      implementation(projects.core.commons)

      implementation(libs.kotlinx.coroutines.core)

      implementation(libs.cryptography.core)

      implementation(libs.datastore)
      implementation(libs.datastore.core)
      implementation(libs.datastore.preferences)
      implementation(libs.datastore.preferences.core)
    }

    androidMain.dependencies {
      implementation(libs.datastore)
      implementation(libs.datastore.preferences)
    }

    nativeMain.dependencies {
      implementation(libs.cryptography.ios)

      implementation(libs.datastore)
      implementation(libs.datastore.preferences)
    }

    commonTest.dependencies {
      implementation(libs.kotlinx.coroutines.test)
    }

    androidHostTest.dependencies {
      implementation(libs.robolectric)
    }
  }
}
