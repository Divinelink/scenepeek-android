package com.divinelink.core.model.preferences

import com.divinelink.core.model.locale.Country

data class DetailPreferences(
  val region: Country,
  val streamingServicesVisible: Boolean,
) {
  companion object {
    val initial = DetailPreferences(
      region = Country.UNITED_STATES,
      streamingServicesVisible = true,
    )
  }
}
