package com.divinelink.core.model.locale

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.language_arabic
import com.divinelink.core.model.resources.language_bulgarian
import com.divinelink.core.model.resources.language_chinese
import com.divinelink.core.model.resources.language_croatian
import com.divinelink.core.model.resources.language_czech
import com.divinelink.core.model.resources.language_danish
import com.divinelink.core.model.resources.language_dutch
import com.divinelink.core.model.resources.language_english
import com.divinelink.core.model.resources.language_estonian
import com.divinelink.core.model.resources.language_finnish
import com.divinelink.core.model.resources.language_french
import com.divinelink.core.model.resources.language_german
import com.divinelink.core.model.resources.language_greek
import com.divinelink.core.model.resources.language_hebrew
import com.divinelink.core.model.resources.language_hindi
import com.divinelink.core.model.resources.language_hungarian
import com.divinelink.core.model.resources.language_indonesian
import com.divinelink.core.model.resources.language_italian
import com.divinelink.core.model.resources.language_japanese
import com.divinelink.core.model.resources.language_korean
import com.divinelink.core.model.resources.language_latvian
import com.divinelink.core.model.resources.language_lithuanian
import com.divinelink.core.model.resources.language_malay
import com.divinelink.core.model.resources.language_norwegian
import com.divinelink.core.model.resources.language_polish
import com.divinelink.core.model.resources.language_portuguese
import com.divinelink.core.model.resources.language_romanian
import com.divinelink.core.model.resources.language_russian
import com.divinelink.core.model.resources.language_serbian
import com.divinelink.core.model.resources.language_slovak
import com.divinelink.core.model.resources.language_slovenian
import com.divinelink.core.model.resources.language_spanish
import com.divinelink.core.model.resources.language_swedish
import com.divinelink.core.model.resources.language_tagalog
import com.divinelink.core.model.resources.language_thai
import com.divinelink.core.model.resources.language_turkish
import com.divinelink.core.model.resources.language_ukrainian
import com.divinelink.core.model.resources.language_vietnamese
import org.jetbrains.compose.resources.StringResource

enum class Language(
  val code: String,
  val nameRes: StringResource,
) {
  ENGLISH("en", Res.string.language_english),
  SPANISH("es", Res.string.language_spanish),
  FRENCH("fr", Res.string.language_french),
  GERMAN("de", Res.string.language_german),
  ITALIAN("it", Res.string.language_italian),
  PORTUGUESE("pt", Res.string.language_portuguese),
  RUSSIAN("ru", Res.string.language_russian),
  CHINESE("zh", Res.string.language_chinese),
  JAPANESE("ja", Res.string.language_japanese),
  KOREAN("ko", Res.string.language_korean),
  ARABIC("ar", Res.string.language_arabic),
  HINDI("hi", Res.string.language_hindi),
  DUTCH("nl", Res.string.language_dutch),
  SWEDISH("sv", Res.string.language_swedish),
  NORWEGIAN("no", Res.string.language_norwegian),
  DANISH("da", Res.string.language_danish),
  FINNISH("fi", Res.string.language_finnish),
  GREEK("el", Res.string.language_greek),
  TURKISH("tr", Res.string.language_turkish),
  POLISH("pl", Res.string.language_polish),
  CZECH("cs", Res.string.language_czech),
  HUNGARIAN("hu", Res.string.language_hungarian),
  ROMANIAN("ro", Res.string.language_romanian),
  BULGARIAN("bg", Res.string.language_bulgarian),
  CROATIAN("hr", Res.string.language_croatian),
  SERBIAN("sr", Res.string.language_serbian),
  SLOVAK("sk", Res.string.language_slovak),
  SLOVENIAN("sl", Res.string.language_slovenian),
  ESTONIAN("et", Res.string.language_estonian),
  LATVIAN("lv", Res.string.language_latvian),
  LITHUANIAN("lt", Res.string.language_lithuanian),
  UKRAINIAN("uk", Res.string.language_ukrainian),
  THAI("th", Res.string.language_thai),
  VIETNAMESE("vi", Res.string.language_vietnamese),
  INDONESIAN("id", Res.string.language_indonesian),
  MALAY("ms", Res.string.language_malay),
  TAGALOG("tl", Res.string.language_tagalog),
  HEBREW("he", Res.string.language_hebrew),
  ;

  companion object {
    private val map = Language.entries.associateBy { it.code }

    fun fromCode(code: String): Language? = map[code]
  }
}
