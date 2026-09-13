package com.divinelink.core.network.media.mapper.certifications

import com.divinelink.core.model.details.release.CountryRelease
import com.divinelink.core.model.locale.Country

fun List<CountryRelease>.getContentRating(selectedRegion: Country?) = with(
  filterNot { it.certification == null },
) {
  find { it.country == selectedRegion }?.certification ?: find {
    it.country == Country.UNITED_STATES
  }?.certification
}
