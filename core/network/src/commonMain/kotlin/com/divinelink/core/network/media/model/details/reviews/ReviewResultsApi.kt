package com.divinelink.core.network.media.model.details.reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResultsApi(
  val author: String,
  @SerialName("author_details") val authorDetails: AuthorDetailsApi,
  val content: String,
  @SerialName("created_at") val createdAt: String,
  val id: String,
  @SerialName("updated_at") val updatedAt: String,
  val url: String,
)
