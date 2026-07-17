package com.divinelink.core.model.locale

import com.divinelink.core.model.UIText
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
  French(
    localeName = "fr",
    text = UIText.StringText("Français"),
  ),
  GERMAN(
    localeName = "de",
    text = UIText.StringText("Deutsch"),
  ),
  ITALIAN(
    localeName = "it",
    text = UIText.StringText("Italiano"),
  ),
  GREEK(
    localeName = "el",
    text = UIText.StringText("Ελληνικά"),
  ),
  ESTONIAN(
    localeName = "et",
    text = UIText.StringText("Eesti keel"),
  ),
  ;

  companion object {
    fun from(locale: String?): AppLanguage = entries
      .find { it.localeName == locale }
      ?: DEFAULT
  }
}
