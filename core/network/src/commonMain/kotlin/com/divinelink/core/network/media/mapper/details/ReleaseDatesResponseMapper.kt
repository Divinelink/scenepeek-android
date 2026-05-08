package com.divinelink.core.network.media.mapper.details

import com.divinelink.core.commons.extensions.toLocalDateTime
import com.divinelink.core.model.details.release.CountryRelease
import com.divinelink.core.model.details.release.ReleaseDetail
import com.divinelink.core.model.details.release.ReleaseType
import com.divinelink.core.model.locale.Country
import com.divinelink.core.network.media.model.details.CountryReleaseResponse
import com.divinelink.core.network.media.model.details.ReleaseDatesResponse
import com.divinelink.core.network.media.model.details.ReleaseDetailResponse

fun ReleaseDatesResponse?.map() = this
  ?.results
  ?.map()

fun List<CountryReleaseResponse>.map() = mapNotNull { it.map() }

fun CountryReleaseResponse.map(): CountryRelease? {
  val country = Country.fromCode(countryCode) ?: return null

  return CountryRelease(
    country = country,
    releaseDetails = releases.mapNotNull { it.map() },
  )
}

fun ReleaseDetailResponse.map(): ReleaseDetail? {
  val type = ReleaseType.from(type) ?: return null
  val releaseDate = releaseDate.toLocalDateTime() ?: return null

  return ReleaseDetail(
    note = note,
    releaseDate = releaseDate,
    type = type,
  )
}
