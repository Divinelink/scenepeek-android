package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.PersonCombinedCreditsEntity
import com.divinelink.core.model.person.credits.PersonCombinedCredits

fun PersonCombinedCreditsEntity.map(
  favoriteMovieIds: List<Int>,
  favoriteTvIds: List<Int>,
) = PersonCombinedCredits(
  id = id,
  cast = cast.map { credit ->
    credit.map(
      isTvFavorite = credit.id.toInt() in favoriteTvIds,
      isMovieFavorite = credit.id.toInt() in favoriteMovieIds,
    )
  },
  crew = crew.map { credit ->
    credit.map(
      isTvFavorite = credit.id.toInt() in favoriteTvIds,
      isMovieFavorite = credit.id.toInt() in favoriteMovieIds,
    )
  },
)
