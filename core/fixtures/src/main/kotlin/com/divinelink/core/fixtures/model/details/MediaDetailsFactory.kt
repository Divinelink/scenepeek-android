package com.divinelink.core.fixtures.model.details

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.rating.RatingCount

object MediaDetailsFactory {

  fun FightClub() = Movie(
    id = 1123,
    posterPath = "123456",
    backdropPath = "123456",
    releaseDate = "2022",
    title = "Flight Club",
    ratingCount = RatingCount.tmdb(7.3, 123_456),
    isFavorite = false,
    overview = "This movie is good.",
    creators = listOf(
      Person(
        id = 123443321,
        name = "Forest Gump",
        profilePath = "BoxOfChocolates.jpg",
        knownForDepartment = "Directing",
        role = listOf(PersonRole.Director),
      ),
    ),
    cast = ActorFactory.all(),
    genres = listOf("Thriller", "Drama", "Comedy"),
    runtime = "2h 10m",
    imdbId = "tt0137523",
    tagline = "You don't talk about Fight Club.",
  )

  fun TheOffice() = TV(
    id = 2316,
    title = "The Office",
    posterPath = "the_office.jpg",
    backdropPath = "the_office_backdrop.jpg",
    overview = "Michael Scarn is the best.",
    releaseDate = "2005-03-24",
    ratingCount = RatingCount.tmdb(9.5, 12_345),
    isFavorite = false,
    genres = listOf("Comedy, Romance"),
    seasons = listOf(),
    creators = listOf(
      Person(
        id = 1216630,
        name = "Greg Daniels",
        profilePath = "/2Hi7Tw0fyYFOZex8BuGsHS8Q4KD.jpg",
        knownForDepartment = "Writing",
        role = listOf(PersonRole.Creator),
      ),
      Person(
        id = 17835,
        name = "Ricky Gervais",
        profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
        knownForDepartment = "Writing",
        role = listOf(PersonRole.Creator),
      ),
    ),
    numberOfSeasons = 9,
    status = TvStatus.ENDED,
    imdbId = "tt0386676",
    tagline = "You don't talk about The Office.",
  )
}
