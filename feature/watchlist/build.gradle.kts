plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.divinelink.android.library.compose)
}

android {
  namespace = "com.divinelink.watchlist"
}

dependencies {
  implementation(projects.core.model)
}
