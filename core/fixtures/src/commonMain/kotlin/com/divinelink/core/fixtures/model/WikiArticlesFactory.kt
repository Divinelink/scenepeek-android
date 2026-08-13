package com.divinelink.core.fixtures.model

import com.divinelink.core.model.locale.LocaleCode

object WikiArticlesFactory {

  val empty = mapOf<LocaleCode, String>()

  val availableWikis = mapOf(
    "ar" to "لا_أرض_أخرى, ",
    "bn" to "নো_আদার_ল্যান্ড",
    "ca" to "Cap_altra_terra",
    "ck" to "ھیچ_خاکێکی_دیکە_نییە",
    "cs" to "Žádná_jiná_země",
    "de" to "No_Other_Land",
    "en" to "No_Other_Land",
    "es" to "No_Other_Land",
    "eu" to "No_Other_Land",
    "fa" to "جز_این_سرزمین_جایی_نیست",
    "fr" to "No_Other_Land",
    "gl" to "No_Other_Land",
    "id" to "No_Other_Land",
    "it" to "No_Other_Land",
    "ja" to "ノー・アザー・ランド_故郷は他にない",
    "ko" to "노_어더_랜드",
    "lv" to "Nav_citas_zemes",
    "nl" to "No_Other_Land",
    "no" to "No_Other_Land",
    "pt" to "No_Other_Land",
    "ru" to "Другой_земли_нет",
    "sl" to "Edina_zemlja",
    "sr" to "Нема_друге_земље",
    "tr" to "No_Other_Land",
    "uk" to "Нема_іншої_землі",
    "zh" to "唯一的家园",
  )
}
