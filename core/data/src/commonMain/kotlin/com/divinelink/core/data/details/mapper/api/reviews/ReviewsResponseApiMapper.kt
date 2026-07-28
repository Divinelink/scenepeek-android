package com.divinelink.core.data.details.mapper.api.reviews

import com.divinelink.core.commons.extensions.toLocalDateTime
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.network.media.model.details.reviews.ReviewResultsApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi

fun ReviewsResponseApi.map(): List<Review> = this.results.map(ReviewResultsApi::map)

private fun ReviewResultsApi.map(): Review = Review(
  author = authorDetails.map(),
  rating = authorDetails.rating?.toInt(),
  content = content,
  date = createdAt.toLocalDateTime()?.date,
  url = url,
)
