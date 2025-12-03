package com.divinelink.core.data.details.mapper.api.reviews

import com.divinelink.core.commons.Constants.ISO_8601
import com.divinelink.core.commons.extensions.formatTo
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.network.media.model.details.reviews.ReviewResultsApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi

fun ReviewsResponseApi.map(): List<Review> = this.results.map(ReviewResultsApi::map)

private fun ReviewResultsApi.map(): Review = Review(
  author = this.authorDetails.map(),
  rating = this.authorDetails.rating?.toInt(),
  content = this.content,
  date = this.createdAt.formatTo(
    inputFormat = ISO_8601,
    outputFormat = "dd-MM-yyyy",
  ),
)
