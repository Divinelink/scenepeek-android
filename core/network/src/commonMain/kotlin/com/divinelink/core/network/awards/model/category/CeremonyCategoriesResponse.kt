package com.divinelink.core.network.awards.model.category

import kotlinx.serialization.Serializable

@Serializable
data class CeremonyCategoriesResponse(
  val categories: List<CeremonyCategoryResponse>,
)
