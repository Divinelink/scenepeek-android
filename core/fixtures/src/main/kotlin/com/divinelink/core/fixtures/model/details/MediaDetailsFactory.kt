package com.divinelink.core.fixtures.model.details

import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.rating.RatingCount

object MediaDetailsFactory {

  fun FightClub() = Movie(
    id = 550,
    posterPath = "/jSziioSwPVrOy9Yow3XhWIBDjq1.jpg",
    backdropPath = "/xRyINp9KfMLVjRiO5nCsoRDdvvF.jpg",
    releaseDate = "1999-10-15",
    title = "Fight Club",
    ratingCount = RatingCount.tmdb(8.4, 30452),
    isFavorite = false,
    overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel " +
      "primal male aggression into a shocking new form of therapy." +
      " Their concept catches on, with underground \"fight clubs\" forming in every town, " +
      "until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
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
    posterPath = "/dg9e5fPRRId8PoBE0F6jl5y85Eu.jpg",
    backdropPath = "/bX6Sypdpk0r8YFdVPoc3yeyvSmm.jpg",
    overview = "The everyday lives of office employees in the Scranton, " +
      "Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    releaseDate = "2005-03-24",
    ratingCount = RatingCount.tmdb(8.6, 4503),
    isFavorite = false,
    genres = listOf("Comedy, Romance"),
    seasons = SeasonFactory.all(),
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
