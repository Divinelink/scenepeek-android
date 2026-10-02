package com.divinelink.core.model.certification


import com.divinelink.core.model.locale.Country


object ShowCertification {

  val map by lazy {
    Country.entries.associateWith { country ->
      when (country) {
        Country.ARGENTINA -> ARGENTINA
        Country.AUSTRALIA -> AUSTRALIA
        Country.BRAZIL -> BRAZIL
        Country.BULGARIA -> BULGARIA
        Country.CANADA -> CANADA
        Country.DENMARK -> DENMARK
        Country.FINLAND -> FINLAND
        Country.FRANCE -> FRANCE
        Country.GERMANY -> GERMANY
        Country.GREECE -> GREECE
        Country.HUNGARY -> HUNGARY
        Country.MOROCCO -> MOROCCO
        Country.INDIA -> INDIA
        Country.INDONESIA -> INDONESIA
        Country.ITALY -> ITALY
        Country.LITHUANIA -> LITHUANIA
        Country.MALAYSIA -> MALAYSIA
        Country.MEXICO -> MEXICO
        Country.NETHERLANDS -> NETHERLANDS
        Country.NEW_ZEALAND -> NEW_ZEALAND
        Country.NORWAY -> NORWAY
        Country.PHILIPPINES -> PHILIPPINES
        Country.POLAND -> POLAND
        Country.PORTUGAL -> PORTUGAL
        Country.RUSSIA -> RUSSIA
        Country.SINGAPORE -> SINGAPORE
        Country.SLOVAKIA -> SLOVAKIA
        Country.SOUTH_AFRICA -> SOUTH_AFRICA
        Country.SOUTH_KOREA -> SOUTH_KOREA
        Country.SPAIN -> SPAIN
        Country.SWEDEN -> SWEDEN
        Country.TAIWAN -> TAIWAN
        Country.THAILAND -> THAILAND
        Country.TURKEY -> TURKEY
        Country.UNITED_KINGDOM -> UNITED_KINGDOM
        Country.UNITED_STATES -> UNITED_STATES
        else -> UNITED_STATES
      }
    }
  }

  private val ARGENTINA: List<Certification> = listOf(
    Certification("ATP", 1),
    Certification("SAM 13", 2),
    Certification("SAM 16", 3),
    Certification("SAM 18", 4),
  )

  private val AUSTRALIA: List<Certification> = listOf(
    Certification("P", 1),
    Certification("C", 2),
    Certification("G", 3),
    Certification("PG", 4),
    Certification("M", 5),
    Certification("MA 15+", 6),
    Certification("AV 15+", 7),
    Certification("R 18+", 8),
  )

  private val BRAZIL: List<Certification> = listOf(
    Certification("L", 0),
    Certification("10", 1),
    Certification("12", 2),
    Certification("14", 3),
    Certification("16", 4),
    Certification("18", 5),
  )

  private val BULGARIA: List<Certification> = listOf(
    Certification("Unrated", 1),
    Certification("12", 2),
    Certification("14", 3),
    Certification("16", 4),
    Certification("18", 5),
  )

  private val CANADA: List<Certification> = listOf(
    Certification("Exempt", 1),
    Certification("C", 2),
    Certification("C8", 3),
    Certification("G", 4),
    Certification("PG", 5),
    Certification("14+", 6),
    Certification("18+", 7),
  )

  private val DENMARK: List<Certification> = listOf(
    Certification("A", 1),
    Certification("7", 2),
    Certification("11", 3),
    Certification("15", 4),
  )

  private val FINLAND: List<Certification> = listOf(
    Certification("S", 0),
    Certification("K7", 1),
    Certification("K12", 2),
    Certification("K16", 3),
    Certification("K18", 4),
  )

  private val FRANCE: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("10", 1),
    Certification("12", 2),
    Certification("16", 3),
    Certification("18", 4),
  )

  private val GERMANY: List<Certification> = listOf(
    Certification("0", 1),
    Certification("6", 2),
    Certification("12", 3),
    Certification("16", 4),
    Certification("18", 5),
  )

  private val GREECE: List<Certification> = listOf(
    Certification("K", 1),
    Certification("8", 2),
    Certification("12", 3),
    Certification("16", 4),
    Certification("18", 5),
  )

  private val HUNGARY: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("Unrated", 1),
    Certification("Children", 2),
    Certification("6", 3),
    Certification("12", 4),
    Certification("16", 5),
    Certification("18", 6),
  )

  private val INDIA: List<Certification> = listOf(
    Certification("U", 1),
    Certification("U/A 7+", 2),
    Certification("U/A 13+", 3),
    Certification("U/A 16+", 4),
    Certification("A", 5),
  )

  private val INDONESIA: List<Certification> = listOf(
    Certification("SU", 1),
    Certification("P", 2),
    Certification("A", 3),
    Certification("R", 4),
    Certification("D", 5),
  )

  private val ITALY: List<Certification> = listOf(
    Certification("T", 1),
    Certification("BA", 2),
    Certification("VM12", 3),
    Certification("VM14", 4),
    Certification("VM18", 5),
  )

  private val LITHUANIA: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("N-7", 1),
    Certification("N-14", 2),
    Certification("S", 3),
  )

  private val MALAYSIA: List<Certification> = listOf(
    Certification("U", 1),
    Certification("P13", 2),
    Certification("18", 3),
  )

  private val MEXICO: List<Certification> = listOf(
    Certification("AA", 1),
    Certification("A", 2),
    Certification("B", 3),
    Certification("B-15", 4),
    Certification("C", 5),
    Certification("D", 6),
  )

  private val MOROCCO: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("10", 1),
    Certification("12", 2),
    Certification("16", 3),
  )

  private val NETHERLANDS: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("AL", 1),
    Certification("6", 2),
    Certification("9", 3),
    Certification("12", 4),
    Certification("14", 5),
    Certification("16", 6),
    Certification("18", 7),
  )

  private val NEW_ZEALAND: List<Certification> = listOf(
    Certification("G", 1),
    Certification("PG", 2),
    Certification("M", 3),
    Certification("16", 4),
    Certification("18", 5),
  )

  private val NORWAY: List<Certification> = listOf(
    Certification("A", 1),
    Certification("6", 2),
    Certification("9", 3),
    Certification("12", 4),
    Certification("15", 5),
    Certification("18", 6),
  )

  private val PHILIPPINES: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("G", 1),
    Certification("PG", 2),
    Certification("SPG", 3),
    Certification("X", 4),
  )

  private val POLAND: List<Certification> = listOf(
    Certification("0", 1),
    Certification("7", 2),
    Certification("12", 3),
    Certification("16", 4),
    Certification("18", 5),
  )

  private val PORTUGAL: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("T", 1),
    Certification("10AP", 2),
    Certification("12AP", 3),
    Certification("16", 4),
    Certification("18", 5),
  )

  private val RUSSIA: List<Certification> = listOf(
    Certification("0+", 1),
    Certification("6+", 2),
    Certification("12+", 3),
    Certification("16+", 4),
    Certification("18+", 5),
  )

  private val SINGAPORE: List<Certification> = listOf(
    Certification("G", 1),
    Certification("PG", 2),
    Certification("PG13", 3),
    Certification("NC16", 4),
    Certification("M18", 5),
    Certification("R21", 6),
  )

  private val SLOVAKIA: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("U", 1),
    Certification("7", 2),
    Certification("12", 3),
    Certification("15", 4),
    Certification("18", 5),
  )

  private val SOUTH_AFRICA: List<Certification> = listOf(
    Certification("All", 1),
    Certification("PG", 2),
    Certification("13", 3),
    Certification("16", 4),
    Certification("18", 5),
    Certification("X18", 6),
  )

  private val SOUTH_KOREA: List<Certification> = listOf(
    Certification("Exempt", 1),
    Certification("ALL", 2),
    Certification("7", 3),
    Certification("12", 4),
    Certification("15", 5),
    Certification("19", 6),
  )

  private val SPAIN: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("ERI", 1),
    Certification("TP", 2),
    Certification("7", 3),
    Certification("10", 4),
    Certification("12", 5),
    Certification("13", 6),
    Certification("16", 7),
    Certification("18", 8),
  )

  private val SWEDEN: List<Certification> = listOf(
    Certification("Btl", 1),
    Certification("Från 7 år", 2),
    Certification("Från 11 år", 3),
    Certification("Från 15 år", 4),
  )

  private val TAIWAN: List<Certification> = listOf(
    Certification("0+", 1),
    Certification("6+", 2),
    Certification("12+", 3),
    Certification("15+", 4),
    Certification("18+", 4),
  )

  private val THAILAND: List<Certification> = listOf(
    Certification("ส", 1),
    Certification("ท", 2),
    Certification("น 13+", 3),
    Certification("น 15+", 4),
    Certification("น 18+", 5),
    Certification("ฉ 20-", 6),
    Certification("-", 7),
  )

  private val TURKEY: List<Certification> = listOf(
    Certification("Genel İzleyici", 1),
    Certification("7+", 2),
    Certification("13+", 3),
    Certification("18+", 4),
  )

  private val UNITED_KINGDOM: List<Certification> = listOf(
    Certification("U", 0),
    Certification("PG", 1),
    Certification("12A", 2),
    Certification("12", 3),
    Certification("15", 4),
    Certification("18", 5),
    Certification("R18", 6),
  )
  val UNITED_STATES: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("TV-Y", 1),
    Certification("TV-Y7", 2),
    Certification("TV-G", 3),
    Certification("TV-PG", 4),
    Certification("TV-14", 5),
    Certification("TV-MA", 6),
  )
}
