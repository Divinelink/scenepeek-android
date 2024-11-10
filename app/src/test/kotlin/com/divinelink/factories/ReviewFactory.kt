package com.divinelink.factories

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.model.details.Review

object ReviewFactory {

  fun Review_1() = Review(
    authorName = "Eledriel S.",
    rating = 10,
    content = LoremIpsum(100).values.joinToString(),
    date = "13-02-2017",
  )

  fun Review_2() = Review(
    authorName = "Andreas O.",
    rating = 6,
    content = LoremIpsum(80).values.joinToString(),
    date = "13-02-2018",
  )

  fun Review_3() = Review(
    authorName = "John Doe",
    rating = 9,
    content = LoremIpsum(60).values.joinToString(),
    date = "13-02-2019",
  )

  fun all() = listOf(
    Review_1(),
    Review_2(),
    Review_3(),
  )

  fun ReviewList(range: IntProgression = 1..10): List<Review> = (range).map {
    Review(
      authorName = "authorName $it",
      rating = it,
      content = "content $it",
      date = "date $it",
    )
  }
}
