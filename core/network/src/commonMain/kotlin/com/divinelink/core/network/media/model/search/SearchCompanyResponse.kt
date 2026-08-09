package com.divinelink.core.network.media.model.search

import com.divinelink.core.network.media.model.details.ProductionCompanyResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchCompanyResponse(
  val page: Int,
  val results: List<ProductionCompanyResponse>,
  @SerialName("total_pages") val totalPages: Int,
  @SerialName("total_results") val totalResults: Int,
)
