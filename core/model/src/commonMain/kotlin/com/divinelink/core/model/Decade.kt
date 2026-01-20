package com.divinelink.core.model

enum class Decade(val startYear: Int, val endYear: Int, val label: String) {
  DECADE_1900(1900, 1909, "1900s"),
  DECADE_1910(1910, 1919, "10s"),
  DECADE_1920(1920, 1929, "20s"),
  DECADE_1930(1930, 1939, "30s"),
  DECADE_1940(1940, 1949, "40s"),
  DECADE_1950(1950, 1959, "50s"),
  DECADE_1960(1960, 1969, "60s"),
  DECADE_1970(1970, 1979, "70s"),
  DECADE_1980(1980, 1989, "80s"),
  DECADE_1990(1990, 1999, "90s"),
  DECADE_2000(2000, 2009, "2000s"),
  DECADE_2010(2010, 2019, "2010s"),
  DECADE_2020(2020, 2029, "2020s"),
  ;

  companion object {
    fun getDecadeLabel(decadeStart: Int): String = entries
      .find { it.startYear == decadeStart }?.label ?: "${decadeStart}s"
  }
}
