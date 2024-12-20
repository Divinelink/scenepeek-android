package com.divinelink.factories.api

import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.factories.CreditsFactory
import com.divinelink.factories.GenreFactory

object DetailsResponseApiFactory {

  fun Movie() = DetailsResponseApi.Movie(
    adult = false,
    backdropPath = "",
    belongToCollection = null,
    budget = 0,
    genres = GenreFactory.all(),
    homepage = null,
    id = 1123,
    imdbId = null,
    originalLanguage = "",
    originalTitle = "",
    overview = "This movie is good.",
    popularity = 0.0,
    posterPath = "123456",
    productionCompanies = listOf(),
    productionCountries = listOf(),
    releaseDate = "2022",
    revenue = 0,
    runtime = 130,
    spokenLanguage = listOf(),
    status = null,
    tagline = "",
    title = "Flight Club",
    video = false,
    voteAverage = 7.316,
    voteCount = 123_456,
    credits = CreditsFactory.all(),
  )
}
