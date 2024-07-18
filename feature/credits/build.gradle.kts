plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.divinelink.android.library.compose)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.serialization)
}

ksp {
  arg("compose-destinations.moduleName", "feature:credits")

  arg("compose-destinations.htmlMermaidGraph", "$rootDir/docs")
  arg("compose-destinations.mermaidGraph", "$rootDir/docs")

  arg("compose-destinations.codeGenPackageName", "com.divinelink.feature.credits.screens")
}

dependencies {
  implementation(libs.kotlinx.serialization.json)
  implementation(projects.core.domain)

  testImplementation(projects.core.testing)
}
