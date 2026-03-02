package com.divinelink.core.model.preferences

import com.divinelink.core.model.locale.Country

data class DetailPreferences(
  val region: Country,
  val streamProvidersVisible: Boolean,
) {
  companion object {
    val initial = DetailPreferences(
      region = Country.UNITED_STATES,
      streamProvidersVisible = true,
    )
  }
}
