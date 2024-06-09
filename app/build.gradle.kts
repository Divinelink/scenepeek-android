@file:Suppress("MagicNumber")

plugins {
  alias(libs.plugins.divinelink.android.application)
  alias(libs.plugins.divinelink.android.application.compose)

  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.android.application)
  alias(libs.plugins.detekt)
  alias(libs.plugins.firebase.appdistribution)
  alias(libs.plugins.firebase.crashlytics)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.gms)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ktlint)
}

apply("../buildscripts/detekt.gradle")
apply("../buildscripts/git-hooks.gradle")
apply("../buildscripts/kover.gradle")
apply("../buildscripts/coveralls.gradle")

android {
  namespace = "com.andreolas.movierama"

  defaultConfig {
    applicationId = "com.andreolas.movierama"
    versionCode = 6
    versionName = "0.2.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {

    signingConfigs {
      create("release") {
        val tmpFilePath = System.getProperty("user.home") + "/work/_temp/keystore/"
        val allFilesFromDir = File(tmpFilePath).listFiles()
        if (allFilesFromDir != null) {
          val keystoreFile = allFilesFromDir.first()
          keystoreFile.renameTo(File("/keystore/keystore.jks"))
          storeFile = keystoreFile
        }
        storePassword = System.getenv("SIGNING_STORE_PASSWORD")
        keyAlias = System.getenv("SIGNING_KEY_ALIAS")
        keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
      }
    }
  }

  buildTypes {
    debug {
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      applicationIdSuffix = ".debug"
      versionNameSuffix = " DEBUG"
    }
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
      firebaseAppDistribution {
        artifactType = "APK"
        artifactPath = "app/build/outputs/apk/release/app-release.apk"
        groups = "development"
      }
    }
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }

  lint {
    checkReleaseBuilds = false
    abortOnError = false
  }

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementation(projects.core.commons)
  implementation(projects.core.data)
  implementation(projects.core.database)
  implementation(projects.core.designsystem)
  implementation(projects.core.model)
  implementation(projects.core.network)
  implementation(projects.core.ui)

  // Firebase
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.config)
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)

  implementation(libs.androidx.startup)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)

  implementation(libs.kotlin.test.junit)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.coroutines.core)

  implementation(platform(libs.compose.bom))
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.ui.tooling.preview)
  implementation(libs.compose.hilt.navigation)

  implementation(libs.compose.coil)

  debugImplementation(libs.compose.ui.tooling.preview)
  debugImplementation(libs.compose.ui.test.manifest)

  // Misc
  implementation(libs.timber)
  implementation(libs.compose.destinations.core)
  implementation(libs.compose.destinations.bottom.sheet)
  ksp(libs.compose.destinations.ksp)

  // Video Players
  implementation(libs.youtube.player)

  // Network & Serialization
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.kotlinx.serialization.json)

  // DI
  ksp(libs.dagger.hilt.compiler)
  implementation(libs.dagger.hilt.android)
  kspTest(libs.dagger.hilt.compiler)
  testImplementation(libs.dagger.hilt.android.testing)
  androidTestImplementation(libs.dagger.hilt.android.testing)
  kspAndroidTest(libs.dagger.hilt.compiler)
  kspAndroidTest(libs.dagger.hilt.android.compiler)

  // Database
  implementation(libs.datastore)
  implementation(libs.datastore.core)
  implementation(libs.datastore.preferences)
  implementation(libs.datastore.preferences.core)
  implementation(libs.encrypted.preferences)
  implementation(libs.room.ktx) // TODO Remove room deps and add di for database
  implementation(libs.room.runtime)
  ksp(libs.room.compiler)

  // Testing Libs
  testImplementation(projects.core.testing)

  testImplementation(libs.junit)
  testImplementation(libs.mockito)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.truth)
  testImplementation(libs.turbine)
  testImplementation(libs.androidx.test.ktx)

  testImplementation(libs.androidx.compose.ui.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.espresso)
  testImplementation(libs.ui.automator)
  debugImplementation(libs.androidx.test.ktx)

  testImplementation(libs.kotlin.test.junit)

  androidTestImplementation(libs.androidx.compose.ui.test)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso)
  androidTestImplementation(libs.truth)
  androidTestImplementation(libs.ui.automator)
}
