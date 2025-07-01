package com.divinelink.core.network.account.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListsResponse(
  val page: Int,
  val results: List<ListItemResponse>,
  @SerialName("total_pages") val totalPages: Int,
  @SerialName("total_results") val totalResults: Int,
) {
  @Serializable
  data class ListItemResponse(
    @SerialName("account_object_id") val accountObjectId: String,
    val adult: Int,
    @SerialName("average_rating") val averageRating: Double,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("created_at") val createdAt: String,
    val description: String,
    val featured: Int,
    val id: Int,
    @SerialName("iso_3166_1") val iso31661: String,
    @SerialName("iso_639_1") val iso6391: String,
    val name: String,
    @SerialName("number_of_items") val numberOfItems: Int,
    @SerialName("poster_path") val posterPath: String?,
    val public: Int,
    val revenue: Long,
    val runtime: String,
    @SerialName("sort_by") val sortBy: Int,
    @SerialName("updated_at") val updatedAt: String,
  )
}
