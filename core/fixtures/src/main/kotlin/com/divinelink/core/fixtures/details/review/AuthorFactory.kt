package com.divinelink.core.fixtures.details.review

import com.divinelink.core.model.details.review.Author

object AuthorFactory {

  fun jeffrey() = Author(
    name = "Jeffrey Dean Morgan",
    avatarPath = "https://image.tmdb.org/t/",
    username = "Winchester",
  )

  fun eledriel() = Author(
    name = "Eledriel S.",
    username = "eledriel_s",
    avatarPath = "eledriel_s.jpg",
  )

  fun andreas() = Author(
    name = "Andreas O.",
    username = "andreas_o",
    avatarPath = "andreas_o.jpg",
  )

  fun john() = Author(
    name = "John Doe",
    username = "john_doe",
    avatarPath = "john_doe.jpg",
  )

  fun empty() = Author(
    name = "",
    avatarPath = "",
    username = "",
  )
}
