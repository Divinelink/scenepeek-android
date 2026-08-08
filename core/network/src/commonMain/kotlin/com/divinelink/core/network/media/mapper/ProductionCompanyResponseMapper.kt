package com.divinelink.core.network.media.mapper

import com.divinelink.core.model.details.Company
import com.divinelink.core.model.locale.Country
import com.divinelink.core.network.media.model.details.ProductionCompanyResponse

fun ProductionCompanyResponse.map(): Company? {
  val originalCountry = originalCountry
  val logoPath = logoPath

  if (originalCountry.isNullOrEmpty()) return null
  if (logoPath.isNullOrEmpty()) return null
  val country = Country.fromCode(originalCountry) ?: return null

  return Company(
    id = id,
    name = name,
    logoPath = logoPath,
    country = country,
  )
}
