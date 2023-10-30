package com.andreolas.factories

import com.andreolas.movierama.details.domain.model.Director
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.TVDetails

object MediaDetailsFactory {

  fun FightClub() = MovieDetails(
    id = 1123,
    posterPath = "123456",
    releaseDate = "2022",
    title = "Flight Club",
    rating = "7.3",
    isFavorite = false,
    overview = "This movie is good.",
    director = Director(id = 123443321, name = "Forest Gump", profilePath = "BoxOfChocolates.jpg"),
    cast = ActorFactory.all(),
    genres = listOf("Thriller", "Drama", "Comedy"),
    runtime = "2h 10m",
  )

  fun TheOffice() = TVDetails(
    id = 6159,
    title = "The Office",
    posterPath = "the_office.jpg",
    overview = "Michael Scarn is the best.",
    director = Director(id = 123443321, name = "Forest Gump", profilePath = "BoxOfChocolates.jpg"),
    cast = ActorFactory.all(),
    releaseDate = "2005-03-24",
    rating = "9.5",
    isFavorite = false,
    seasons = listOf()
  )
}
