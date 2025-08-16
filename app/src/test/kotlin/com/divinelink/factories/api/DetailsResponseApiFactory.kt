@file:Suppress("LongMethod")

package com.divinelink.factories.api

import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.ProductionCompany
import com.divinelink.core.network.media.model.details.ProductionCountryResponse
import com.divinelink.factories.CreditsFactory
import com.divinelink.factories.GenreFactory

object DetailsResponseApiFactory {

  fun Movie() = DetailsResponseApi.Movie(
    adult = false,
    backdropPath = "/xRyINp9KfMLVjRiO5nCsoRDdvvF.jpg",
    belongToCollection = null,
    budget = 63000000,
    genres = GenreFactory.all(),
    homepage = null,
    id = 550,
    imdbId = "tt0137523",
    originalLanguage = "en",
    status = "Released",
    originalTitle = "Fight Club",
    overview = "A ticking-time-bomb insomniac and a slippery soap salesman" +
      " channel primal male aggression into a shocking new form of therapy. " +
      "Their concept catches on, with underground \"fight clubs\" forming in every town, " +
      "until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
    popularity = 21.6213,
    posterPath = "/jSziioSwPVrOy9Yow3XhWIBDjq1.jpg",
    companies = listOf(
      ProductionCompany(
        id = 711,
        logoPath = "/tEiIH5QesdheJmDAqQwvtN60727.png",
        name = "Fox 2000 Pictures",
        originalCountry = "US",
      ),
      ProductionCompany(
        id = 508,
        logoPath = "/4sGWXoboEkWPphI6es6rTmqkCBh.png",
        name = "Regency Enterprises",
        originalCountry = "US",
      ),
      ProductionCompany(
        id = 4700,
        logoPath = "/A32wmjrs9Psf4zw0uaixF0GXfxq.png",
        name = "Linson Entertainment",
        originalCountry = "US",
      ),
      ProductionCompany(
        id = 25,
        logoPath = "/qZCc1lty5FzX30aOCVRBLzaVmcp.png",
        name = "20th Century Fox",
        originalCountry = "US",
      ),
      ProductionCompany(
        id = 20555,
        logoPath = "/hD8yEGUBlHOcfHYbujp71vD8gZp.png",
        name = "Taurus Film",
        originalCountry = "DE",
      ),
    ),
    countries = listOf(
      ProductionCountryResponse(
        iso31611 = "DE",
        name = "Germany",
      ),
      ProductionCountryResponse(
        iso31611 = "US",
        name = "Unites States of America",
      ),
    ),
    releaseDate = "1999-10-15",
    revenue = 100853753,
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
