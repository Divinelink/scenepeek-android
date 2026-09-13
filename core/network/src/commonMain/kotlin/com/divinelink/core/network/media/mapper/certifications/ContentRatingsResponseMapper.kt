package com.divinelink.core.network.media.mapper.certifications

import com.divinelink.core.model.locale.Country
import com.divinelink.core.network.media.model.details.certifications.ContentRatingsResponse

fun ContentRatingsResponse.map(selectedRegion: Country?): String? = results
  .find { it.iso31661 == selectedRegion?.code }?.rating.takeIf { !it.isNullOrBlank() } ?: results
  .find { it.iso31661 == Country.UNITED_STATES.code }?.rating.takeIf { !it.isNullOrBlank() }
