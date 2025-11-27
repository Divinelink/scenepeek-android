import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.data)
      api(projects.core.model)
      api(projects.core.fixtures)
      implementation(projects.core.ui)
      implementation(projects.core.designsystem)
      implementation(projects.core.commons)
      implementation(projects.core.domain)
      implementation(projects.core.datastore)
      implementation(projects.core.scaffold)

      api(libs.kotlinx.coroutines.test)
      api(libs.turbine)
      api(libs.koin.test)
      api(libs.ktor.client.mock)
      api(libs.kotest.assertions)

      api(libs.datastore)
      api(libs.datastore.core)
      api(libs.datastore.preferences)
      api(libs.datastore.preferences.core)

      implementation(libs.mockito)
      implementation(libs.mockito.kotlin)

      implementation(libs.kotlinx.datetime)

      @OptIn(ExperimentalComposeLibrary::class)
      implementation(compose.uiTest)

      implementation(libs.kotlinx.coroutines.core)
    }

    iosMain.dependencies {
      implementation(libs.sqldelight.native)
    }

    androidMain.dependencies {
      api(libs.kotlin.test.junit)
      api(libs.truth)

      api(libs.compose.ui.test.junit4)
      api(libs.compose.ui.test.manifest)

      implementation(libs.sqldelight.driver)

      implementation(libs.robolectric)
    }
  }
}

dependencies {
  debugApi(libs.compose.ui.test.manifest)
}

