// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.compose) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.kover) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.firebase.crashlytics) apply false
  alias(libs.plugins.firebase.appdistribution) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.sqldelight) apply false
  alias(libs.plugins.gms) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.screenshot) apply false
}

// Temporary fix for screenshot testing library
allprojects {
  configurations.all {
    resolutionStrategy.eachDependency {
      if (requested.group == "com.google.guava" && requested.name == "guava") {
        useVersion("33.2.0-jre") // Force a stable, recent version
        because("Align Guava to prevent runtime errors.")
      }
    }
  }
}
