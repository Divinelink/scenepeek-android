package com.divinelink.core.model.details

import com.divinelink.core.model.locale.Country
import kotlinx.serialization.Serializable

@Serializable
data class Company(
  val id: Long,
  val name: String,
  val logoPath: String,
  val country: Country,
)
