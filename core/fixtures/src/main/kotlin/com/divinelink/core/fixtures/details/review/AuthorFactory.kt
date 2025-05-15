package com.divinelink.core.fixtures.details.review

import com.divinelink.core.model.details.review.Author

object AuthorFactory {

  fun jeffrey() = Author(
    name = "Jeffrey Dean Morgan",
    avatarPath = "https://image.tmdb.org/t/",
    username = "Winchester",
  )

  fun empty() = Author(
    name = "",
    avatarPath = "",
    username = "",
  )
}
