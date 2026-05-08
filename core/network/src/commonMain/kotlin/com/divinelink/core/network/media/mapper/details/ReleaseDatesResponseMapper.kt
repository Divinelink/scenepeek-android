package com.divinelink.core.network.media.mapper.details

import com.divinelink.core.commons.extensions.toLocalDateTime
import com.divinelink.core.model.details.release.CountryRelease
import com.divinelink.core.model.details.release.ReleaseDetail
import com.divinelink.core.model.details.release.ReleaseType
import com.divinelink.core.model.locale.Country
import com.divinelink.core.network.media.model.details.CountryReleaseResponse
import com.divinelink.core.network.media.model.details.ReleaseDatesResponse
import com.divinelink.core.network.media.model.details.ReleaseDetailResponse

fun ReleaseDatesResponse?.map(selectedRegion: Country?) = this
  ?.results
  ?.mapToSortedReleases(selectedRegion)

fun List<CountryReleaseResponse>.mapToSortedReleases(
  selectedRegion: Country?,
): List<CountryRelease> = flatMap { release ->
  release.releases.mapNotNull { detail ->
    val country = Country.fromCode(release.countryCode) ?: return@mapNotNull null
    val releaseDetail = detail.map() ?: return@mapNotNull null

    CountryRelease(
      country = country,
      releaseDetail = releaseDetail,
    )
  }
}.sortedWith(
  compareBy { item ->
    when (item.country) {
      selectedRegion -> 0
      Country.UNITED_STATES -> 1
      else -> 2
    }
  },
)

fun ReleaseDetailResponse.map(): ReleaseDetail? {
  val type = ReleaseType.from(type) ?: return null
  val releaseDate = releaseDate.toLocalDateTime() ?: return null

  return ReleaseDetail(
    note = note,
    releaseDate = releaseDate,
    type = type,
  )
}
