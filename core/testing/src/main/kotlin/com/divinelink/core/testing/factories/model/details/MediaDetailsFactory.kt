package com.divinelink.core.testing.factories.model.details

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV

object MediaDetailsFactory {

  fun FightClub() = Movie(
    id = 1123,
    posterPath = "123456",
    releaseDate = "2022",
    title = "Flight Club",
    rating = "7.3",
    isFavorite = false,
    overview = "This movie is good.",
    director = Person(
      id = 123443321,
      name = "Forest Gump",
      profilePath = "BoxOfChocolates.jpg",
      knownForDepartment = "Directing",
      role = PersonRole.Director,
    ),
    cast = ActorFactory.all(),
    genres = listOf("Thriller", "Drama", "Comedy"),
    runtime = "2h 10m",
  )

  fun TheOffice() = TV(
    id = 2316,
    title = "The Office",
    posterPath = "the_office.jpg",
    overview = "Michael Scarn is the best.",
    credits = ActorFactory.all(),
    releaseDate = "2005-03-24",
    rating = "9.5",
    isFavorite = false,
    genres = listOf("Comedy, Romance"),
    seasons = listOf(),
    creators = listOf(
      Person(
        id = 1216630,
        name = "Greg Daniels",
        profilePath = "/2Hi7Tw0fyYFOZex8BuGsHS8Q4KD.jpg",
        knownForDepartment = "Writing",
        role = PersonRole.Creator,
      ),
      Person(
        id = 17835,
        name = "Ricky Gervais",
        profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
        knownForDepartment = "Writing",
        role = PersonRole.Creator,
      ),
    ),
    numberOfSeasons = 9,
  )
}
