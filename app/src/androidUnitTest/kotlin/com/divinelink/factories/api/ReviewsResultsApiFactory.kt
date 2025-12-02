package com.divinelink.factories.api

import com.divinelink.core.fixtures.loremIpsum
import com.divinelink.core.network.media.model.details.reviews.AuthorDetailsApi
import com.divinelink.core.network.media.model.details.reviews.ReviewResultsApi

object ReviewsResultsApiFactory {

  fun Review_1() = ReviewResultsApi(
    author = "Eledriel S.",
    authorDetails = AuthorDetailsApi(
      avatarPath = "eledriel_s.jpg",
      name = "Eledriel S.",
      rating = 10.0,
      username = "eledriel_s",
    ),
    content = loremIpsum(100),
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
      username = "andreas_o",
    ),
    content = loremIpsum(80),
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
      username = "john_doe",
    ),
    content = loremIpsum(60),
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
