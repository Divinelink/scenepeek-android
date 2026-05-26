package com.divinelink.core.model

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.system_default

/**
 * Represents the languages supported by the app.
 */
enum class AppLanguage(
  val localeName: String?,
  val text: UIText,
) {
  DEFAULT(
    localeName = null,
    text = UIText.ResourceText(Res.string.system_default),
  ),
  ENGLISH(
    localeName = "en",
    text = UIText.StringText("English"),
  ),
  GERMAN(
    localeName = "de",
    text = UIText.StringText("Deutsch"),
  ),
  GREEK(
    localeName = "el",
    text = UIText.StringText("Ελληνικά"),
  ),
  ;

  companion object {
    fun from(locale: String?): AppLanguage = AppLanguage
      .entries
      .find { it.localeName == locale }
      ?: DEFAULT
  }
}
