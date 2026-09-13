package com.divinelink.core.testing.factories.api.media

import com.divinelink.core.network.media.model.details.CountryReleaseResponse
import com.divinelink.core.network.media.model.details.ReleaseDatesResponse
import com.divinelink.core.network.media.model.details.ReleaseDetailResponse

object ReleaseDatesResponseFactory {
  val fightClub = ReleaseDatesResponse(
    results = listOf(
      CountryReleaseResponse(
        countryCode = "AE",
        releases = listOf(
          ReleaseDetailResponse(
            certification = "",
            note = "Blu-ray",
            languageCode = "",
            releaseDate = "2009-11-23T00:00:00.000Z",
            type = 5,
          ),
        ),
      ),
      CountryReleaseResponse(
        countryCode = "AR",
        releases = listOf(
          ReleaseDetailResponse(
            certification = "+18",
            note = "",
            languageCode = "",
            releaseDate = "1999-11-04T00:00:00.000Z",
            type = 3,
          ),
        ),
      ),
      CountryReleaseResponse(
        countryCode = "CA",
        releases = listOf(
          ReleaseDetailResponse(
            certification = "18A",
            note = "4K Remaster",
            languageCode = "",
            releaseDate = "2026-04-22T00:00:00.000Z",
            type = 2,
          ),
          ReleaseDetailResponse(
            certification = "18A",
            note = "",
            languageCode = "",
            releaseDate = "1999-10-15T00:00:00.000Z",
            type = 3,
          ),
        ),
      ),
      CountryReleaseResponse(
        countryCode = "CH",
        releases = listOf(
          ReleaseDetailResponse(
            certification = "18",
            note = "",
            languageCode = "fr",
            releaseDate = "1999-11-10T00:00:00.000Z",
            type = 3,
          ),
          ReleaseDetailResponse(
            certification = "18",
            note = "",
            languageCode = "de",
            releaseDate = "1999-11-04T00:00:00.000Z",
            type = 3,
          ),
        ),
      ),
      CountryReleaseResponse(
        countryCode = "FR",
        releases = listOf(
          ReleaseDetailResponse(
            certification = "16",
            note = "",
            languageCode = "",
            releaseDate = "1999-11-10T00:00:00.000Z",
            type = 3,
          ),
          ReleaseDetailResponse(
            certification = "",
            note = "Re-Release",
            languageCode = "",
            releaseDate = "2019-05-15T00:00:00.000Z",
            type = 3,
          ),
          ReleaseDetailResponse(
            certification = "16",
            note = "Netflix",
            languageCode = "",
            releaseDate = "2021-10-01T00:00:00.000Z",
            type = 4,
          ),
          ReleaseDetailResponse(
            certification = "16",
            note = "Canal+ Grand Écran",
            languageCode = "fr",
            releaseDate = "2026-06-03T00:00:00.000Z",
            type = 6,
          ),
        ),
      ),
      CountryReleaseResponse(
        countryCode = "GB",
        releases = listOf(
          ReleaseDetailResponse(
            certification = "18",
            note = "",
            languageCode = "",
            releaseDate = "1999-11-12T00:00:00.000Z",
            type = 3,
          ),
          ReleaseDetailResponse(
            certification = "15",
            note = "25th Anniversary",
            languageCode = "",
            releaseDate = "2024-03-15T00:00:00.000Z",
            type = 3,
          ),
          ReleaseDetailResponse(
            certification = "18",
            note = "4k Ultra HD Blu-ray, Steelbook",
            languageCode = "",
            releaseDate = "2026-05-18T00:00:00.000Z",
            type = 5,
          ),
        ),
      ),
      CountryReleaseResponse(
        countryCode = "TR",
        releases = listOf(
          ReleaseDetailResponse(
            certification = "18+",
            note = "",
            languageCode = "",
            releaseDate = "1999-12-10T00:00:00.000Z",
            type = 3,
          ),
        ),
      ),
      CountryReleaseResponse(
        countryCode = "US",
        releases = listOf(
          ReleaseDetailResponse(
            certification = "",
            note = "CMJ Film Festival",
            languageCode = "",
            releaseDate = "1999-09-21T00:00:00.000Z",
            type = 1,
          ),
          ReleaseDetailResponse(
            certification = "R",
            note = "4K Remaster",
            languageCode = "",
            releaseDate = "2026-04-22T00:00:00.000Z",
            type = 2,
          ),
          ReleaseDetailResponse(
            certification = "R",
            note = "",
            languageCode = "",
            releaseDate = "1999-10-15T00:00:00.000Z",
            type = 3,
          ),
          ReleaseDetailResponse(
            certification = "R",
            note = "4K Digital",
            languageCode = "",
            releaseDate = "2026-05-12T00:00:00.000Z",
            type = 4,
          ),
          ReleaseDetailResponse(
            certification = "R",
            note = "4K Ultra HD SteelBook",
            languageCode = "",
            releaseDate = "2026-05-12T00:00:00.000Z",
            type = 5,
          ),
        ),
      ),
    ),
  )
}
