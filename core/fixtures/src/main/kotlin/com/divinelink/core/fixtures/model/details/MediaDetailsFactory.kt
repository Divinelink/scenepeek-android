package com.divinelink.core.fixtures.model.details

import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.media.MediaDetailsInformation
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.person.Gender

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
    popularity = 21.6213,
    information = MediaDetailsInformation.Movie(
      originalTitle = "Fight Club",
      status = "Released",
      runtime = "2h 10m",
      originalLanguage = Language.ENGLISH,
      budget = "$63,000,000",
      revenue = "$100,853,753",
      companies = listOf(
        "Fox 2000 Pictures",
        "Regency Enterprises",
        "Linson Entertainment",
        "20th Century Fox",
        "Taurus Film",
      ),
      countries = listOf(
        Country.GERMANY,
        Country.UNITED_STATES,
      ),
    ),
  )

  fun TheOffice() = TV(
    id = 2316,
    title = "The Office",
    posterPath = "/7DJKHzAi83BmQrWLrYYOqcoKfhR.jpg",
    backdropPath = "/mLyW3UTgi2lsMdtueYODcfAB9Ku.jpg",
    overview = "The everyday lives of office employees in the Scranton, " +
      "Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    releaseDate = "2005-03-24",
    ratingCount = RatingCount.tmdb(8.6, 4503),
    isFavorite = false,
    genres = listOf("Comedy"),
    seasons = SeasonFactory.all(),
    creators = listOf(
      Person(
        id = 17835,
        name = "Ricky Gervais",
        profilePath = "/2mAjcq9AQA9peQxNoeEW76DPIju.jpg",
        knownForDepartment = null,
        gender = Gender.MALE,
        role = listOf(PersonRole.Creator),
      ),
      Person(
        id = 39189,
        name = "Stephen Merchant",
        profilePath = "/6WM2rK6390Nwk398syJbGcPvRct.jpg",
        knownForDepartment = null,
        gender = Gender.MALE,
        role = listOf(PersonRole.Creator),
      ),
      Person(
        id = 1216630,
        name = "Greg Daniels",
        profilePath = "/2Hi7Tw0fyYFOZex8BuGsHS8Q4KD.jpg",
        knownForDepartment = null,
        gender = Gender.MALE,
        role = listOf(PersonRole.Creator),
      ),
    ),
    numberOfSeasons = 9,
    imdbId = "tt0386676",
    tagline = "A comedy for anyone whose boss is an idiot.",
    popularity = 530.095,
    information = MediaDetailsInformation.TV(
      originalTitle = "The Office",
      status = TvStatus.ENDED,
      firstAirDate = "2005-03-24",
      lastAirDate = "2013-05-16",
      seasons = 9,
      episodes = 186,
      originalLanguage = Language.ENGLISH,
      companies = listOf(
        "Universal Television",
        "Deedle-Dee Productions",
        "Reveille Productions",
        "Shine America",
        "Universal Media Studios",
      ),
      countries = listOf(
        Country.UNITED_STATES,
      ),
      nextEpisodeAirDate = null,
    ),
  )
}
