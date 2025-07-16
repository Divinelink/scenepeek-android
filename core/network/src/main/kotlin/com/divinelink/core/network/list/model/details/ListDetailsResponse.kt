package com.divinelink.core.network.list.model.details

import com.divinelink.core.network.media.model.search.multi.MultiSearchResultApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDetailsResponse(
  val id: Int,
  @SerialName("backdrop_path") val backdropPath: String?,
  @SerialName("average_rating") val averageRating: Double,
  val results: List<MultiSearchResultApi>,
  val name: String,
  val description: String,
  val page: Int,
  val public: Boolean,
  @SerialName("total_pages") val totalPages: Int,
  @SerialName("total_results") val totalResults: Int,
)
