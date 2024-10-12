package com.divinelink.core.network.media.model.details.reviews

import com.divinelink.core.commons.Constants.ISO_8601
import com.divinelink.core.commons.extensions.formatTo
import com.divinelink.core.model.details.Review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsResponseApi(
  val id: Int,
  val page: Int,
  val results: List<ReviewResultsApi>,
  @SerialName("total_pages")
  val totalPages: Int,
  @SerialName("total_results")
  val totalResults: Int,
)

fun ReviewsResponseApi.toDomainReviewsList(): List<Review> =
  this.results.map(ReviewResultsApi::toReview)

private fun ReviewResultsApi.toReview(): Review = Review(
  authorName = this.author,
  rating = this.authorDetails.rating?.toInt(),
  content = this.content,
  date = this.createdAt.formatTo(
    inputFormat = ISO_8601,
    outputFormat = "dd-MM-yyyy",
  ),
)

@Serializable
data class ReviewResultsApi(
  val author: String,
  @SerialName("author_details")
  val authorDetails: AuthorDetailsApi,
  val content: String,
  @SerialName("created_at")
  val createdAt: String,
  val id: String,
  @SerialName("updated_at")
  val updatedAt: String,
  val url: String,
)

@Serializable
data class AuthorDetailsApi(
  @SerialName("avatar_path")
  val avatarPath: String?,
  val name: String,
  val rating: Double?,
  val username: String,
)
