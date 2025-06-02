plugins {
  alias(libs.plugins.divinelink.android.application)
  alias(libs.plugins.divinelink.android.application.compose)
  alias(libs.plugins.divinelink.android.koin)

  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.android.application)
  alias(libs.plugins.detekt)
  alias(libs.plugins.firebase.appdistribution)
  alias(libs.plugins.firebase.crashlytics)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.gms)
  alias(libs.plugins.ktlint)
}

apply("../buildscripts/detekt.gradle")
apply("../buildscripts/git-hooks.gradle")
apply("../buildscripts/kover.gradle")
apply("../buildscripts/coveralls.gradle")

android {
  namespace = "com.divinelink.scenepeek"

  defaultConfig {
    applicationId = "com.divinelink.scenepeek"
    versionCode = libs.versions.version.code.get().toInt()
    versionName = libs.versions.version.name.get()

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
  implementation(projects.core.datastore)
  implementation(projects.core.designsystem)
  implementation(projects.core.domain)
  implementation(projects.core.model)
  implementation(projects.core.network)
  implementation(projects.core.ui)
  implementation(projects.core.scaffold)

  implementation(projects.feature.watchlist)
  implementation(projects.feature.details)
  implementation(projects.feature.search)
  implementation(projects.feature.settings)
  implementation(projects.feature.credits)
  implementation(projects.feature.tmdbAuth)
  implementation(projects.feature.onboarding)

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

  debugImplementation(libs.compose.ui.tooling.preview)
  debugImplementation(libs.compose.ui.test.manifest)

  // Misc
  implementation(libs.timber)

  // Navigation
  implementation(libs.androidx.navigation.runtime.ktx)
  implementation(libs.androidx.navigation.compose)

  // Network & Serialization
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.koin.android.compose)
  implementation(libs.koin.start.up)

  // Database
  implementation(libs.room.ktx) // TODO Remove room deps and add di for database
  implementation(libs.room.runtime)
  ksp(libs.room.compiler)

  // Testing Libs
  testImplementation(projects.core.testing)

  testImplementation(libs.junit)
  testImplementation(libs.mockito)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.truth)
  testImplementation(libs.turbine)
  testImplementation(libs.androidx.test.ktx)

  testImplementation(libs.androidx.compose.ui.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.ui.automator)
  debugImplementation(libs.androidx.test.ktx)

  testImplementation(libs.kotlin.test.junit)

  androidTestImplementation(libs.androidx.compose.ui.test)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.truth)
  androidTestImplementation(libs.ui.automator)
}
