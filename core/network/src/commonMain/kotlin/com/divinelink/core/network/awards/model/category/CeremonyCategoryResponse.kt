package com.divinelink.core.network.awards.model.category

import kotlinx.serialization.Serializable

@Serializable
data class CeremonyCategoryResponse(
  val id: String,
  val title: String,
)
