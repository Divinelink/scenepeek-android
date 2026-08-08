package com.divinelink.core.model.app

enum class InstallSource(
  val source: List<String>,
  val versionCheckUrl: String?,
  val updateUrl: String?,
  val versionRegex: String,
) {
  GooglePlay(
    source = listOf(
      "com.android.vending",
      "com.aurora.store",
    ),
    versionCheckUrl = "https://play.google.com/store/apps/details?id=com.divinelink.scenepeek",
    updateUrl = "https://play.google.com/store/apps/details?id=com.divinelink.scenepeek",
    versionRegex = """\[\["(\d+\.\d+[\d.]*)"]]""",
  ),
  Fdroid(
    source = listOf(
      "org.fdroid.fdroid",
      "org.fdroid.basic",
      "com.machiav3lli.fdroid",
      "com.looker.droidify",
      "app.flicky",
    ),
    versionCheckUrl = "https://gitlab.com/fdroid/fdroiddata/-/raw/master/metadata/" +
      "com.divinelink.scenepeek.yml",
    updateUrl = "https://f-droid.org/en/packages/com.divinelink.scenepeek/",
    versionRegex = "CurrentVersion:\\s*(\\d+\\.\\d+\\.\\d+)",
  ),
  Codeberg(
    source = emptyList(),
    versionCheckUrl = "https://codeberg.org/Divinelink/scenepeek/releases/latest",
    updateUrl = "https://codeberg.org/Divinelink/scenepeek/releases/latest",
    versionRegex = """(v\d+\.\d+[\d.]*)""",
  ),
  AppStore(
    source = emptyList(),
    versionCheckUrl = "https://apps.apple.com/us/app/scenepeek-tmdb-seerr/id6756810408",
    updateUrl = "https://apps.apple.com/us/app/scenepeek-tmdb-seerr/id6756810408",
    versionRegex = """\[\["(\d+\.\d+[\d.]*)"]]""",
  ),
  ;

  companion object {
    fun from(source: String?) = entries.find { source in it.source } ?: Codeberg
  }
}
