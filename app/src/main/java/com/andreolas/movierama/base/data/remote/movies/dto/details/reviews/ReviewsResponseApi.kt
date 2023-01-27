package com.andreolas.movierama.base.data.remote.movies.dto.details.reviews

import com.andreolas.movierama.details.domain.model.Review
import gr.divinelink.core.util.extensions.formatTo
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

internal fun ReviewsResponseApi.toDomainReviewsList(): List<Review> {
    return this.results.map(ReviewResultsApi::toReview)
}

private fun ReviewResultsApi.toReview(): Review {
    return Review(
        authorName = this.author,
        rating = this.authorDetails.rating?.toInt(),
        content = this.content,
        date = this.createdAt.formatTo(
            inputFormat = TMDB_DATE_FORMAT,
            outputFormat = "dd-MM-yyyy"
        )
    )
}

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

private const val TMDB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
