package com.andreolas.movierama.factories.api

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.AuthorDetailsApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewResultsApi

object ReviewsResultsApiFactory {

  fun Review_1() = ReviewResultsApi(
    author = "Eledriel S.",
    authorDetails = AuthorDetailsApi(
      avatarPath = "eledriel_s.jpg",
      name = "Eledriel S.",
      rating = 10.0,
      username = "eledriel_s"
    ),
    content = LoremIpsum(100).values.joinToString(),
    createdAt = "2017-02-13T23:16:19.538Z",
    id = "",
    updatedAt = "",
    url = "",
  )

  fun Review_2() = ReviewResultsApi(
    author = "Andreas O.",
    authorDetails = AuthorDetailsApi(
      avatarPath = "andreas_o.jpg",
      name = "Andreas O.",
      rating = 6.7,
      username = "andreas_o"
    ),
    content = LoremIpsum(80).values.joinToString(),
    createdAt = "2018-02-13T23:16:19.538Z",
    id = "",
    updatedAt = "",
    url = "",
  )

  fun Review_3() = ReviewResultsApi(
    author = "John Doe",
    authorDetails = AuthorDetailsApi(
      avatarPath = "john_doe.jpg",
      name = "John Doe",
      rating = 9.3,
      username = "john_doe"
    ),
    content = LoremIpsum(60).values.joinToString(),
    createdAt = "2019-02-13T23:16:19.538Z",
    id = "",
    updatedAt = "",
    url = "",
  )

  fun all() = listOf(
    Review_1(),
    Review_2(),
    Review_3(),
  )

}
