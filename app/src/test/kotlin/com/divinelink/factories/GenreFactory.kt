package com.divinelink.factories

import com.divinelink.core.network.media.model.details.Genre

object GenreFactory {

  fun Thriller() = Genre(
    id = 0,
    name = "Thriller",
  )

  fun Drama() = Genre(
    id = 1,
    name = "Drama",
  )

  fun Comedy() = Genre(
    id = 2,
    name = "Comedy",
  )

  fun all() = listOf(
    Thriller(),
    Drama(),
    Comedy(),
  )
}
