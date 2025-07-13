package com.divinelink.factories.api

import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.factories.CreditsFactory
import com.divinelink.factories.GenreFactory

object DetailsResponseApiFactory {

  fun Movie() = DetailsResponseApi.Movie(
    adult = false,
    backdropPath = "/xRyINp9KfMLVjRiO5nCsoRDdvvF.jpg",
    belongToCollection = null,
    budget = 0,
    genres = GenreFactory.all(),
    homepage = null,
    id = 550,
    imdbId = "tt0137523",
    originalLanguage = "",
    originalTitle = "",
    overview = "A ticking-time-bomb insomniac and a slippery soap salesman" +
      " channel primal male aggression into a shocking new form of therapy. " +
      "Their concept catches on, with underground \"fight clubs\" forming in every town, " +
      "until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
    popularity = 20.9349,
    posterPath = "/jSziioSwPVrOy9Yow3XhWIBDjq1.jpg",
    productionCompanies = listOf(),
    productionCountries = listOf(),
    releaseDate = "1999-10-15",
    revenue = 0,
    runtime = 130,
    spokenLanguage = listOf(),
    tagline = "You don't talk about Fight Club.",
    title = "Fight Club",
    video = false,
    voteAverage = 8.438,
    voteCount = 30_452,
    credits = CreditsFactory.all(),
  )
}
