package com.divinelink.core.model.details.release

import com.divinelink.core.model.locale.Country

data class CountryRelease(
  val country: Country,
  val releaseDetail: ReleaseDetail,
)
