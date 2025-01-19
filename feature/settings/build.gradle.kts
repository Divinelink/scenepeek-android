plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.divinelink.android.library.compose)
}

ksp {
  arg("compose-destinations.moduleName", "settings")

  arg("compose-destinations.htmlMermaidGraph", "$rootDir/docs")
  arg("compose-destinations.mermaidGraph", "$rootDir/docs")

  arg("compose-destinations.codeGenPackageName", "com.divinelink.feature.settings.screens")
}

dependencies {
  implementation(projects.core.commons)
  implementation(projects.core.domain)
  implementation(projects.core.datastore)

  implementation(libs.androidx.browser)

  implementation(projects.core.fixtures)
  testImplementation(projects.core.testing)
}
