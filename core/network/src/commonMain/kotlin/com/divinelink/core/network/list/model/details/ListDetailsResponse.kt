package com.divinelink.core.network.list.model.details

import com.divinelink.core.network.media.model.search.multi.MultiSearchResultApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDetailsResponse(
  val id: Int,
  @SerialName("backdrop_path") val backdropPath: String?,
  @SerialName("poster_path") val posterPath: String? = null,
  @SerialName("average_rating") val averageRating: Double,
  val results: List<MultiSearchResultApi>,
  val name: String,
  val description: String,
  val page: Int,
  val public: Boolean,
  @SerialName("total_pages") val totalPages: Int,
  @SerialName("total_results") val totalResults: Int,
  @SerialName("item_count") val itemCount: Int,
  val revenue: Long,
  @SerialName("created_by") val createdBy: CreatedByUserResponse,
  @SerialName("sort_by") val sortBy: String,
  @SerialName("iso_3166_1") val iso31661: String,
  @SerialName("iso_639_1") val iso6391: String,
  @SerialName("runtime") val runtime: Int,
)
