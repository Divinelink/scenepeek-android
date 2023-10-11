package com.andreolas.movierama.factories

import com.andreolas.movierama.details.domain.model.Review

object ReviewFactory {

  fun Review() = Review(
    authorName = "authorName",
    rating = 10,
    content = "content",
    date = "date",
  )

  fun ReviewList(
    range: IntProgression = 1..10,
  ): List<Review> = (range).map {
    Review(
      authorName = "authorName $it",
      rating = it,
      content = "content $it",
      date = "date $it",
    )
  }
}
