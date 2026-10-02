package com.divinelink.core.model.certification

import com.divinelink.core.model.locale.Country

object MovieCertification {

  val map by lazy {
    Country.entries.associateWith { country ->
      when (country) {
        Country.ARGENTINA -> ARGENTINA
        Country.AUSTRALIA -> AUSTRALIA
        Country.AUSTRIA -> AUSTRIA
        Country.BELGIUM -> BELGIUM
        Country.BRAZIL -> BRAZIL
        Country.BULGARIA -> BULGARIA
        Country.CANADA -> CANADA
        Country.CHILE -> CHILE
        Country.CZECHIA -> CZECHIA
        Country.DENMARK -> DENMARK
        Country.FINLAND -> FINLAND
        Country.FRANCE -> FRANCE
        Country.GERMANY -> GERMANY
        Country.GREECE -> GREECE
        Country.HONG_KONG -> HONG_KONG
        Country.HUNGARY -> HUNGARY
        Country.INDIA -> INDIA
        Country.INDONESIA -> INDONESIA
        Country.IRELAND -> IRELAND
        Country.ITALY -> ITALY
        Country.JAPAN -> JAPAN
        Country.LATVIA -> LATVIA
        Country.LITHUANIA -> LITHUANIA
        Country.LUXEMBOURG -> LUXEMBOURG
        Country.MALAYSIA -> MALAYSIA
        Country.MEXICO -> MEXICO
        Country.NETHERLANDS -> NETHERLANDS
        Country.NEW_ZEALAND -> NEW_ZEALAND
        Country.NORWAY -> NORWAY
        Country.PHILIPPINES -> PHILIPPINES
        Country.POLAND -> POLAND
        Country.PORTUGAL -> PORTUGAL
        Country.ROMANIA -> ROMANIA
        Country.RUSSIA -> RUSSIA
        Country.SINGAPORE -> SINGAPORE
        Country.SLOVAKIA -> SLOVAKIA
        Country.SOUTH_AFRICA -> SOUTH_AFRICA
        Country.SOUTH_KOREA -> SOUTH_KOREA
        Country.SPAIN -> SPAIN
        Country.SWEDEN -> SWEDEN
        Country.SWITZERLAND -> SWITZERLAND
        Country.TAIWAN -> TAIWAN
        Country.THAILAND -> THAILAND
        Country.TURKEY -> TURKEY
        Country.UKRAINE -> UKRAINE
        Country.UNITED_KINGDOM -> UNITED_KINGDOM
        Country.UNITED_STATES -> UNITED_STATES
        else -> UNITED_STATES
      }
    }
  }

  private val ARGENTINA: List<Certification> = listOf(
    Certification("ATP", 1),
    Certification("+13", 2),
    Certification("+16", 3),
    Certification("+18", 4),
    Certification("C", 5),
  )
  private val AUSTRALIA: List<Certification> = listOf(
    Certification("G", 1),
    Certification("PG", 2),
    Certification("M", 3),
    Certification("MA 15+", 4),
    Certification("R 18+", 5),
    Certification("X 18+", 6),
    Certification("RC", 7),
  )
  private val AUSTRIA: List<Certification> = listOf(
    Certification("AA", 1),
    Certification("6", 2),
    Certification("8", 3),
    Certification("10", 4),
    Certification("12", 5),
    Certification("14", 6),
    Certification("16", 7),
  )
  private val BELGIUM: List<Certification> = listOf(
    Certification("AL", 1),
    Certification("6", 2),
    Certification("9", 3),
    Certification("12", 4),
    Certification("14", 5),
    Certification("16", 6),
    Certification("18", 7),
  )
  private val BRAZIL: List<Certification> = listOf(
    Certification("L", 1),
    Certification("10", 2),
    Certification("12", 3),
    Certification("14", 4),
    Certification("16", 5),
    Certification("18", 6),
  )
  private val BULGARIA: List<Certification> = listOf(
    Certification("A", 1),
    Certification("B", 2),
    Certification("C", 3),
    Certification("D", 4),
    Certification("X", 5),
  )
  private val CANADA: List<Certification> = listOf(
    Certification("E", 1),
    Certification("G", 2),
    Certification("PG", 3),
    Certification("14A", 4),
    Certification("18A", 5),
    Certification("R", 6),
    Certification("A", 7),
  )
  private val CHILE: List<Certification> = listOf(
    Certification("UR", 1),
    Certification("TE", 2),
    Certification("TE+7", 3),
    Certification("14", 4),
    Certification("18", 5),
  )
  private val CZECHIA: List<Certification> = listOf(
    Certification("UR", 0),
    Certification("U", 1),
    Certification("12+", 2),
    Certification("15+", 3),
    Certification("18+", 4),
  )
  private val DENMARK: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("A", 1),
    Certification("7", 2),
    Certification("11", 3),
    Certification("15", 4),
    Certification("F", 5),
  )
  private val FINLAND: List<Certification> = listOf(
    Certification("S", 1),
    Certification("K-7", 2),
    Certification("K-12", 3),
    Certification("K-16", 4),
    Certification("K-18", 5),
    Certification("KK", 6),
  )
  private val FRANCE: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("TP", 1),
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
    Certification("K12", 2),
    Certification("K15", 3),
    Certification("K18", 4),
  )
  private val HONG_KONG: List<Certification> = listOf(
    Certification("I", 1),
    Certification("IIA", 2),
    Certification("IIB", 3),
    Certification("III", 4),
  )
  private val HUNGARY: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("KN", 1),
    Certification("6", 2),
    Certification("12", 3),
    Certification("16", 4),
    Certification("18", 5),
    Certification("X", 6),
  )
  private val INDIA: List<Certification> = listOf(
    Certification("U", 1),
    Certification("U/A 7+", 2),
    Certification("UA", 3),
    Certification("U/A 13+", 4),
    Certification("U/A 16+", 5),
    Certification("A", 6),
    Certification("S", 7),
  )
  private val INDONESIA: List<Certification> = listOf(
    Certification("SU", 1),
    Certification("13+", 2),
    Certification("17+", 3),
    Certification("21+", 4),
  )
  private val IRELAND: List<Certification> = listOf(
    Certification("G", 1),
    Certification("PG", 2),
    Certification("12A", 3),
    Certification("12", 3),
    Certification("15A", 4),
    Certification("15", 4),
    Certification("16", 5),
    Certification("18", 6),
  )
  private val ITALY: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("T", 1),
    Certification("6+", 2),
    Certification("10+", 3),
    Certification("14+", 4),
    Certification("18+", 5),
  )
  private val JAPAN: List<Certification> = listOf(
    Certification("G", 1),
    Certification("PG12", 2),
    Certification("R15+", 3),
    Certification("R18+", 4),
  )
  private val LATVIA: List<Certification> = listOf(
    Certification("U", 1),
    Certification("7+", 2),
    Certification("12+", 3),
    Certification("16+", 4),
    Certification("18+", 5),
  )
  private val LITHUANIA: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("V", 1),
    Certification("N-7", 2),
    Certification("N-13", 3),
    Certification("N-16", 4),
    Certification("N-18", 5),
  )
  private val LUXEMBOURG: List<Certification> = listOf(
    Certification("A", 1),
    Certification("6", 2),
    Certification("12", 3),
    Certification("16", 4),
    Certification("18", 5),
  )
  private val MALAYSIA: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("U", 1),
    Certification("P13", 2),
    Certification("18SG", 3),
    Certification("18SX", 4),
    Certification("18PA", 5),
    Certification("18PL", 6),
  )
  private val MEXICO: List<Certification> = listOf(
    Certification("AA", 1),
    Certification("A", 2),
    Certification("B", 3),
    Certification("B-15", 4),
    Certification("C", 5),
    Certification("D", 6),
  )
  private val NETHERLANDS: List<Certification> = listOf(
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
    Certification("R13", 4),
    Certification("RP13", 5),
    Certification("R15", 6),
    Certification("R16", 7),
    Certification("RP16", 8),
    Certification("R18", 9),
    Certification("RP18", 10),
    Certification("R", 11),
  )
  private val NORWAY: List<Certification> = listOf(
    Certification("NR", 0),
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
    Certification("R-13", 3),
    Certification("R-16", 4),
    Certification("R-18", 5),
    Certification("X", 6),
  )
  private val POLAND: List<Certification> = listOf(
    Certification("AL", 1),
    Certification("7", 2),
    Certification("12", 3),
    Certification("16", 4),
    Certification("18", 5),
  )
  private val PORTUGAL: List<Certification> = listOf(
    Certification("Públicos", 1),
    Certification("M/3", 2),
    Certification("M/6", 3),
    Certification("M/12", 4),
    Certification("M/14", 5),
    Certification("M/16", 6),
    Certification("M/18", 7),
    Certification("P", 8),
  )
  private val ROMANIA: List<Certification> = listOf(
    Certification("AP", 1),
    Certification("12", 2),
    Certification("15", 3),
    Certification("18", 4),
    Certification("18+", 5),
    Certification("IC", 6),
  )
  private val RUSSIA: List<Certification> = listOf(
    Certification("NR", 0),
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
    Certification("U", 1),
    Certification("7", 2),
    Certification("12", 3),
    Certification("15", 4),
    Certification("18", 5),
  )
  private val SOUTH_AFRICA: List<Certification> = listOf(
    Certification("A", 1),
    Certification("PG", 2),
    Certification("7-9PG", 3),
    Certification("10-12PG", 4),
    Certification("13", 5),
    Certification("16", 6),
    Certification("18", 7),
    Certification("X18", 8),
    Certification("XX", 9),
  )
  private val SOUTH_KOREA: List<Certification> = listOf(
    Certification("ALL", 0),
    Certification("12", 1),
    Certification("15", 2),
    Certification("19", 3),
    Certification("Restricted Screening", 4),
  )
  private val SPAIN: List<Certification> = listOf(
    Certification("A", 1),
    Certification("Ai", 2),
    Certification("7", 3),
    Certification("7i", 4),
    Certification("12", 5),
    Certification("16", 5),
    Certification("18", 7),
    Certification("X", 8),
  )
  private val SWEDEN: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("Btl", 1),
    Certification("7", 2),
    Certification("11", 3),
    Certification("15", 4),
  )
  private val SWITZERLAND: List<Certification> = listOf(
    Certification("0", 1),
    Certification("6", 2),
    Certification("8", 3),
    Certification("10", 4),
    Certification("12", 5),
    Certification("14", 6),
    Certification("16", 7),
    Certification("18", 8),
  )
  private val TAIWAN: List<Certification> = listOf(
    Certification("0+", 1),
    Certification("6+", 2),
    Certification("12+", 3),
    Certification("15+", 4),
    Certification("18+", 5),
  )
  private val THAILAND: List<Certification> = listOf(
    Certification("P", 1),
    Certification("G", 2),
    Certification("13", 3),
    Certification("15", 4),
    Certification("18", 5),
    Certification("20", 6),
    Certification("Banned", 7),
  )
  private val TURKEY: List<Certification> = listOf(
    Certification("Genel İzleyici Kitlesi", 1),
    Certification("6A", 2),
    Certification("6+", 3),
    Certification("10A", 4),
    Certification("10+", 5),
    Certification("13A", 6),
    Certification("13+", 7),
    Certification("16+", 8),
    Certification("18+", 9),
  )
  private val UKRAINE: List<Certification> = listOf(
    Certification("0+", 1),
    Certification("12+", 2),
    Certification("16+", 3),
    Certification("18+", 4),
  )
  private val UNITED_KINGDOM: List<Certification> = listOf(
    Certification("U", 1),
    Certification("PG", 2),
    Certification("12A", 3),
    Certification("12", 4),
    Certification("15", 5),
    Certification("18", 6),
    Certification("R18", 7),
  )
  val UNITED_STATES: List<Certification> = listOf(
    Certification("NR", 0),
    Certification("G", 1),
    Certification("PG", 2),
    Certification("PG-13", 3),
    Certification("R", 4),
    Certification("NC-17", 5),
  )
}
