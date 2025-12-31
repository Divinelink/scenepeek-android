plugins {
  alias(libs.plugins.android.kmp.library)

  alias(libs.plugins.divinelink.kotlin.multiplatform)
  alias(libs.plugins.divinelink.compose.multiplatform)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.core.commons)
      api(projects.core.data)
      api(projects.core.database)
      api(projects.core.datastore)
      api(projects.core.designsystem)
      api(projects.core.domain)
      api(projects.core.model)
      api(projects.core.network)
      api(projects.core.ui)
      api(projects.core.scaffold)

      implementation(projects.feature.addToAccount)
      implementation(projects.feature.details)
      implementation(projects.feature.search)
      implementation(projects.feature.settings)
      implementation(projects.feature.credits)
      implementation(projects.feature.discover)
      implementation(projects.feature.home)
      implementation(projects.feature.tmdbAuth)
      implementation(projects.feature.onboarding)
      implementation(projects.feature.profile)
      implementation(projects.feature.requests)
      implementation(projects.feature.userData)
      implementation(projects.feature.lists)
      implementation(projects.feature.webview)
      implementation(projects.feature.requestMedia)
    }

    androidMain.dependencies {
      implementation(libs.koin.start.up)
    }

    commonTest.dependencies {
      implementation(libs.compose.multiplatform.ui.test)
    }

    androidHostTest.dependencies {
      implementation(libs.androidx.navigation.testing)
      implementation(libs.robolectric)
    }
  }
}

compose.resources {
  publicResClass = false
  packageOfResClass = "com.divinelink.scenepeek.resources"
  generateResClass = auto
}
