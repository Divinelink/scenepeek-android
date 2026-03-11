package com.divinelink.core.model.app

enum class InstallSource(
  val source: String?,
  val versionCheckUrl: String?,
  val versionRegex: String,
) {
  GooglePlay(
    source = "com.android.vending",
    versionCheckUrl = "https://play.google.com/store/apps/details?id=com.divinelink.scenepeek",
    versionRegex = """\[\["(\d+\.\d+[\d.]*)"]]""",
  ),
  Fdroid(
    source = "org.fdroid.fdroid",
    versionCheckUrl = null,
    versionRegex = "",
  ),
  Github(
    source = null,
    versionCheckUrl = "https://github.com/Divinelink/scenepeek-android/releases/latest",
    versionRegex = """(v\d+\.\d+[\d.]*)""",
  ),
  AppStore(
    source = null,
    versionCheckUrl = null,
    versionRegex = "",
  ),
  ;

  companion object {
    fun from(source: String?) = entries.find { it.source == source } ?: Github
  }
}
