package com.divinelink.core.fixtures.model.details

import com.divinelink.core.model.details.release.CountryRelease
import com.divinelink.core.model.details.release.ReleaseDetail
import com.divinelink.core.model.details.release.ReleaseType
import com.divinelink.core.model.locale.Country
import kotlinx.datetime.LocalDate

object ReleaseDatesFactory {

  val fightClub: List<CountryRelease> = listOf(
    CountryRelease(
      country = Country.UNITED_STATES,
      releaseDetail = ReleaseDetail(
        note = "CMJ Film Festival",
        releaseDate = LocalDate(1999, 9, 21),
        type = ReleaseType.Premiere,
        certification = null,
      ),
      certification = null,
    ),
    CountryRelease(
      country = Country.UNITED_STATES,
      releaseDetail = ReleaseDetail(
        note = "4K Remaster",
        releaseDate = LocalDate(2026, 4, 22),
        type = ReleaseType.TheatricalLimited,
        certification = "R",
      ),
      certification = "R",
    ),
    CountryRelease(
      country = Country.UNITED_STATES,
      releaseDetail = ReleaseDetail(
        note = "",
        releaseDate = LocalDate(1999, 10, 15),
        type = ReleaseType.Theatrical,
        certification = "R",
      ),
      certification = "R",
    ),
    CountryRelease(
      country = Country.UNITED_STATES,
      releaseDetail = ReleaseDetail(
        note = "4K Digital",
        releaseDate = LocalDate(2026, 5, 12),
        type = ReleaseType.Digital,
        certification = "R",
      ),
      certification = "R",
    ),
    CountryRelease(
      country = Country.UNITED_STATES,
      releaseDetail = ReleaseDetail(
        note = "4K Ultra HD SteelBook",
        releaseDate = LocalDate(2026, 5, 12),
        type = ReleaseType.Physical,
        certification = "R",
      ),
      certification = "R",
    ),
    CountryRelease(
      country = Country.UAE,
      releaseDetail = ReleaseDetail(
        note = "Blu-ray",
        releaseDate = LocalDate(2009, 11, 23),
        type = ReleaseType.Physical,
        certification = null,
      ),
      certification = null,
    ),
    CountryRelease(
      country = Country.ARGENTINA,
      releaseDetail = ReleaseDetail(
        note = "",
        releaseDate = LocalDate(1999, 11, 4),
        type = ReleaseType.Theatrical,
        certification = "+18",
      ),
      certification = "+18",
    ),
    CountryRelease(
      country = Country.CANADA,
      releaseDetail = ReleaseDetail(
        note = "4K Remaster",
        releaseDate = LocalDate(2026, 4, 22),
        type = ReleaseType.TheatricalLimited,
        certification = "18A",
      ),
      certification = "18A",
    ),
    CountryRelease(
      country = Country.CANADA,
      releaseDetail = ReleaseDetail(
        note = "",
        releaseDate = LocalDate(1999, 10, 15),
        type = ReleaseType.Theatrical,
        certification = "18A",
      ),
      certification = "18A",
    ),
    CountryRelease(
      country = Country.SWITZERLAND,
      releaseDetail = ReleaseDetail(
        note = "",
        releaseDate = LocalDate(1999, 11, 10),
        type = ReleaseType.Theatrical,
        certification = "18",
      ),
      certification = "18",
    ),
    CountryRelease(
      country = Country.SWITZERLAND,
      releaseDetail = ReleaseDetail(
        note = "",
        releaseDate = LocalDate(1999, 11, 4),
        type = ReleaseType.Theatrical,
        certification = "18",
      ),
      certification = "18",
    ),
    CountryRelease(
      country = Country.FRANCE,
      releaseDetail = ReleaseDetail(
        note = "",
        releaseDate = LocalDate(1999, 11, 10),
        type = ReleaseType.Theatrical,
        certification = "16",
      ),
      certification = "16",
    ),
    CountryRelease(
      country = Country.FRANCE,
      releaseDetail = ReleaseDetail(
        note = "Re-Release",
        releaseDate = LocalDate(2019, 5, 15),
        type = ReleaseType.Theatrical,
        certification = null,
      ),
      certification = null,
    ),
    CountryRelease(
      country = Country.FRANCE,
      releaseDetail = ReleaseDetail(
        note = "Netflix",
        releaseDate = LocalDate(2021, 10, 1),
        type = ReleaseType.Digital,
        certification = "16",
      ),
      certification = "16",
    ),
    CountryRelease(
      country = Country.FRANCE,
      releaseDetail = ReleaseDetail(
        note = "Canal+ Grand Écran",
        releaseDate = LocalDate(2026, 6, 3),
        type = ReleaseType.TV,
        certification = "16",
      ),
      certification = "16",
    ),
    CountryRelease(
      country = Country.UNITED_KINGDOM,
      releaseDetail = ReleaseDetail(
        note = "",
        releaseDate = LocalDate(1999, 11, 12),
        type = ReleaseType.Theatrical,
        certification = "18",
      ),
      certification = "18",
    ),
    CountryRelease(
      country = Country.UNITED_KINGDOM,
      releaseDetail = ReleaseDetail(
        note = "25th Anniversary",
        releaseDate = LocalDate(2024, 3, 15),
        type = ReleaseType.Theatrical,
        certification = "15",
      ),
      certification = "15",
    ),
    CountryRelease(
      country = Country.UNITED_KINGDOM,
      releaseDetail = ReleaseDetail(
        note = "4k Ultra HD Blu-ray, Steelbook",
        releaseDate = LocalDate(2026, 5, 18),
        type = ReleaseType.Physical,
        certification = "18",
      ),
      certification = "18",
    ),
    CountryRelease(
      country = Country.TURKEY,
      releaseDetail = ReleaseDetail(
        note = "",
        releaseDate = LocalDate(1999, 12, 10),
        type = ReleaseType.Theatrical,
        certification = "18+",
      ),
      certification = "18+",
    ),
  )
}
