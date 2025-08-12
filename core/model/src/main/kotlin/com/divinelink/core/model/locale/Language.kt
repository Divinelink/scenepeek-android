package com.divinelink.core.model.locale

import com.divinelink.core.model.R

enum class Language(
  val code: String,
  val nameRes: Int,
) {
  ENGLISH("en", R.string.language_english),
  SPANISH("es", R.string.language_spanish),
  FRENCH("fr", R.string.language_french),
  GERMAN("de", R.string.language_german),
  ITALIAN("it", R.string.language_italian),
  PORTUGUESE("pt", R.string.language_portuguese),
  RUSSIAN("ru", R.string.language_russian),
  CHINESE("zh", R.string.language_chinese),
  JAPANESE("ja", R.string.language_japanese),
  KOREAN("ko", R.string.language_korean),
  ARABIC("ar", R.string.language_arabic),
  HINDI("hi", R.string.language_hindi),
  DUTCH("nl", R.string.language_dutch),
  SWEDISH("sv", R.string.language_swedish),
  NORWEGIAN("no", R.string.language_norwegian),
  DANISH("da", R.string.language_danish),
  FINNISH("fi", R.string.language_finnish),
  GREEK("el", R.string.language_greek),
  TURKISH("tr", R.string.language_turkish),
  POLISH("pl", R.string.language_polish),
  CZECH("cs", R.string.language_czech),
  HUNGARIAN("hu", R.string.language_hungarian),
  ROMANIAN("ro", R.string.language_romanian),
  BULGARIAN("bg", R.string.language_bulgarian),
  CROATIAN("hr", R.string.language_croatian),
  SERBIAN("sr", R.string.language_serbian),
  SLOVAK("sk", R.string.language_slovak),
  SLOVENIAN("sl", R.string.language_slovenian),
  ESTONIAN("et", R.string.language_estonian),
  LATVIAN("lv", R.string.language_latvian),
  LITHUANIAN("lt", R.string.language_lithuanian),
  UKRAINIAN("uk", R.string.language_ukrainian),
  THAI("th", R.string.language_thai),
  VIETNAMESE("vi", R.string.language_vietnamese),
  INDONESIAN("id", R.string.language_indonesian),
  MALAY("ms", R.string.language_malay),
  TAGALOG("tl", R.string.language_tagalog),
  HEBREW("he", R.string.language_hebrew),
  ;

  companion object {
    private val map = Language.entries.associateBy { it.code }

    fun fromCode(code: String): Language? = map[code]
  }
}
