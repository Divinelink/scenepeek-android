package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.PersonCombinedCreditsEntity
import com.divinelink.core.model.person.credits.PersonCombinedCredits

fun PersonCombinedCreditsEntity.map(
  favoriteMovieIds: List<Long>,
  favoriteTvIds: List<Long>,
) = PersonCombinedCredits(
  id = id,
  cast = cast.map { credit ->
    credit.map(
      isTvFavorite = credit.id in favoriteTvIds,
      isMovieFavorite = credit.id in favoriteMovieIds,
    )
  },
  crew = crew.map { credit ->
    credit.map(
      isTvFavorite = credit.id in favoriteTvIds,
      isMovieFavorite = credit.id in favoriteMovieIds,
    )
  },
)
