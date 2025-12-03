package com.divinelink.core.network.media.model.details.reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsResponseApi(
  val id: Int,
  val page: Int,
  val results: List<ReviewResultsApi>,
  @SerialName("total_pages") val totalPages: Int,
  @SerialName("total_results") val totalResults: Int,
)
