package com.divinelink.core.network.account

import app.cash.turbine.test
import com.divinelink.core.network.media.model.movie.MovieResponseApi
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.tv.TvItemApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import com.divinelink.core.testing.network.TestRestClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdAccountServiceTest {

  private lateinit var service: ProdAccountService
  private lateinit var testRestClient: TestRestClient

  @BeforeTest
  fun setUp() {
    testRestClient = TestRestClient()
  }

  @Test
  fun `test fetch movie watchlist`() = runTest {
    testRestClient.mockGetResponse<MoviesResponseApi>(
      url = "https://api.themoviedb.org/3/account/12345/watchlist/movies" +
        "?page=1" +
        "&session_id=session12345" +
        "&sort_by=created_at.desc",
      jsonFileName = "watchlist-movie.json",
    )

    service = ProdAccountService(testRestClient.restClient)

    service.fetchMoviesWatchlist(
      page = 1,
      sortBy = "desc",
      accountId = "12345",
      sessionId = "session12345",
    ).test {
      val response = awaitItem()
      assertThat(response.page).isEqualTo(1)
      assertThat(response.results).hasSize(20)
      assertThat(response.totalPages).isEqualTo(17)
      assertThat(response.totalResults).isEqualTo(328)

      assertThat(response.results.first()).isEqualTo(
        MovieResponseApi(
          adult = false,
          backdropPath = "/4WfwZ5QLbyQccaaTDsJLs1Mt3By.jpg",
          genreIds = listOf(35, 37, 28),
          id = 23330,
          originalLanguage = "en",
          originalTitle = "The Ballad of Cable Hogue",
          overview = "Double-crossed and left without water in the desert," +
            " Cable Hogue is saved when he finds a spring. It is in just" +
            " the right spot for a much needed rest stop on the local " +
            "stagecoach line, and Hogue uses this to his advantage." +
            " He builds a house and makes money off the stagecoach " +
            "passengers. Hildy, a prostitute from the nearest town," +
            " moves in with him. Hogue has everything going his way " +
            "until the advent of the automobile ends the era of the stagecoach.",
          popularity = 1.3443,
          posterPath = "/bEth8XAYaytjzxnHkLkj4nZx2XD.jpg",
          releaseDate = "1970-03-18",
          title = "The Ballad of Cable Hogue",
          video = false,
          voteAverage = 6.969,
          voteCount = 210,
          rating = null,
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetch tv watchlist`() = runTest {
    testRestClient.mockGetResponse<TvResponseApi>(
      url = "https://api.themoviedb.org/3/account/12345/watchlist/tv" +
        "?page=1" +
        "&session_id=session12345" +
        "&sort_by=created_at.desc",
      jsonFileName = "watchlist-tv.json",
    )

    service = ProdAccountService(testRestClient.restClient)

    service.fetchTvShowsWatchlist(
      page = 1,
      sortBy = "desc",
      accountId = "12345",
      sessionId = "session12345",
    ).test {
      val response = awaitItem()
      assertThat(response.page).isEqualTo(1)
      assertThat(response.results).hasSize(20)
      assertThat(response.totalPages).isEqualTo(3)
      assertThat(response.totalResults).isEqualTo(47)

      assertThat(response.results.first()).isEqualTo(
        TvItemApi(
          id = 120998,
          adult = false,
          backdropPath = "/ccOzCmrglAjRGtqv5ClTaBEsWt8.jpg",
          genreIds = listOf(9648, 80),
          originCountry = listOf("US"),
          originalLanguage = "en",
          originalName = "Poker Face",
          overview = "Follow Charlie Cale, a woman with an extraordinary ability to tell" +
            " when someone is lying, as she hits the road and," +
            " at every stop, encounters a new cast of " +
            "characters and crimes she can't help but solve.",
          popularity = 52.6238,
          posterPath = "/sQxgo6z2nEzq1kfXdktRIWtje3B.jpg",
          firstAirDate = "2023-01-26",
          name = "Poker Face",
          voteAverage = 7.613,
          voteCount = 382,
          rating = null,
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetch rated tv`() = runTest {
    testRestClient.mockGetResponse<TvResponseApi>(
      url = "https://api.themoviedb.org/3/account/12345/rated/tv" +
        "?page=1" +
        "&session_id=session12345" +
        "&sort_by=created_at.desc",
      jsonFileName = "rated-tv.json",
    )

    service = ProdAccountService(testRestClient.restClient)

    service.fetchRatedTvShows(
      page = 1,
      sortBy = "desc",
      accountId = "12345",
      sessionId = "session12345",
    ).test {
      val response = awaitItem()
      assertThat(response.page).isEqualTo(1)
      assertThat(response.results).hasSize(17)
      assertThat(response.totalPages).isEqualTo(1)
      assertThat(response.totalResults).isEqualTo(25)

      assertThat(response.results.first()).isEqualTo(
        TvItemApi(
          id = 1396,
          adult = false,
          backdropPath = "/gc8PfyTqzqltKPW3X0cIVUGmagz.jpg",
          genreIds = listOf(18, 80),
          originCountry = listOf("US"),
          originalLanguage = "en",
          originalName = "Breaking Bad",
          overview = "Walter White, a New Mexico chemistry teacher, is diagnosed with " +
            "Stage III cancer and given a prognosis of only two years left to live." +
            " He becomes filled with a sense of fearlessness and an unrelenting desire" +
            " to secure his family's financial future at any cost as he enters " +
            "the dangerous world of drugs and crime.",
          popularity = 163.323,
          posterPath = "/ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg",
          firstAirDate = "2008-01-20",
          name = "Breaking Bad",
          voteAverage = 8.928,
          voteCount = 15715,
          rating = 10,
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetch rated movies`() = runTest {
    testRestClient.mockGetResponse<MoviesResponseApi>(
      url = "https://api.themoviedb.org/3/account/12345/rated/movies" +
        "?page=1" +
        "&session_id=session12345" +
        "&sort_by=created_at.desc",
      jsonFileName = "rated-movies.json",
    )

    service = ProdAccountService(testRestClient.restClient)

    service.fetchRatedMovies(
      page = 1,
      sortBy = "desc",
      accountId = "12345",
      sessionId = "session12345",
    ).test {
      val response = awaitItem()
      assertThat(response.page).isEqualTo(1)
      assertThat(response.results).hasSize(20)
      assertThat(response.totalPages).isEqualTo(36)
      assertThat(response.totalResults).isEqualTo(715)

      assertThat(response.results.first()).isEqualTo(
        MovieResponseApi(
          adult = false,
          backdropPath = "/7Wev9JMo6R5XAfz2KDvXb7oPMmy.jpg",
          genreIds = listOf(9648, 53),
          id = 77,
          originalLanguage = "en",
          originalTitle = "Memento",
          overview = "Leonard Shelby is tracking down the man who raped and murdered his wife. " +
            "The difficulty of locating his wife's killer, however, is compounded by the" +
            " fact that he suffers from a rare, untreatable form of short-term memory loss." +
            " Although he can recall details of life before his accident," +
            " Leonard cannot remember what happened fifteen minutes ago, where he's going, or why.",
          popularity = 10.6783,
          posterPath = "/fKTPH2WvH8nHTXeBYBVhawtRqtR.jpg",
          releaseDate = "2000-10-11",
          title = "Memento",
          video = false,
          voteAverage = 8.179,
          voteCount = 15360,
          rating = 9,
        ),
      )

      awaitComplete()
    }
  }
}
