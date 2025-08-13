package com.divinelink.scenepeek.search.domain.repository

import JvmUnitTestDemoAssetManager
import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.ReviewsException
import com.divinelink.core.data.details.model.SimilarException
import com.divinelink.core.data.details.model.VideosException
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.details.repository.ProdDetailsRepository
import com.divinelink.core.database.credits.dao.ProdCreditsDao
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.details.review.ReviewFactory
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.toMediaItem
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.media.model.details.toDomainMedia
import com.divinelink.core.network.media.model.details.videos.VideoResultsApi
import com.divinelink.core.network.media.model.details.videos.VideosResponseApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.details.watchlist.SubmitOnAccountResponse
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.omdb.model.OMDbResponseApi
import com.divinelink.core.network.trakt.model.TraktRatingApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.dao.TestCreditsDao
import com.divinelink.core.testing.dao.TestMediaDao
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.api.media.MediaRequestApiFactory
import com.divinelink.core.testing.factories.api.movie.MoviesResponseApiFactory
import com.divinelink.core.testing.factories.api.tv.TvResponseApiFactory
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.core.testing.factories.entity.credits.AggregateCreditsEntityFactory
import com.divinelink.core.testing.service.TestMediaService
import com.divinelink.core.testing.service.TestOMDbService
import com.divinelink.core.testing.service.TestTraktService
import com.divinelink.factories.api.DetailsResponseApiFactory
import com.divinelink.factories.api.ReviewsResultsApiFactory
import com.divinelink.factories.api.account.states.AccountMediaDetailsResponseApiFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class ProdDetailsRepositoryTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  val testDispatcher = mainDispatcherRule.testDispatcher

  private val movieDetails = MediaDetailsFactory.FightClub()

  private val detailsResponseApi = DetailsResponseApiFactory.Movie()

  private val reviewsResponseApi = ReviewsResponseApi(
    id = 1,
    page = 1,
    results = ReviewsResultsApiFactory.all(),
    totalPages = 1,
    totalResults = 3,
  )

  private val expectedReviews = ReviewFactory.all()

  private val videoResponseApi = VideosResponseApi(
    id = 1,
    results = listOf(
      VideoResultsApi(
        id = "123",
        iso6391 = "en",
        iso31661 = "US",
        key = "123",
        name = "Lorem Ipsum",
        site = "YouTube",
        size = 1080,
        type = "Trailer",
        official = true,
        publishedAt = "",
      ),
      VideoResultsApi(
        id = "1234",
        iso6391 = "en",
        iso31661 = "US",
        key = "1234",
        name = "Lorem Ipsum",
        site = "Vimeo",
        size = 1080,
        type = "Trailer",
        official = false,
        publishedAt = "",
      ),
      VideoResultsApi(
        id = "567",
        iso6391 = "en",
        iso31661 = "US",
        key = "567",
        name = "Lorem Ipsum",
        site = "Something Else",
        size = 1080,
        type = "Trailer",
        official = true,
        publishedAt = "",
      ),
    ),
  )

  private val creditsResponseApi = JvmUnitTestDemoAssetManager
    .open("credits.json")
    .use { inputStream ->
      val credits = inputStream.readBytes().decodeToString().trimIndent()
      val serializer = AggregateCreditsApi.serializer()
      val fullApi = Json.decodeFromString(serializer, credits)

      AggregateCreditsApi(
        id = fullApi.id,
        cast = fullApi.cast.take(2),
        crew = listOf(fullApi.crew[4], fullApi.crew[5], fullApi.crew[6], fullApi.crew[7]),
      )
    }

  private var mediaRemote = TestMediaService()
  private var creditsDao = TestCreditsDao()
  private var omdbService = TestOMDbService()
  private var traktService = TestTraktService()
  private var mediaDao = TestMediaDao()

  private lateinit var repository: DetailsRepository

  @Before
  fun setUp() {
    repository = ProdDetailsRepository(
      mediaRemote = mediaRemote.mock,
      creditsDao = creditsDao.mock,
      omdbService = omdbService.mock,
      traktService = traktService.mock,
      mediaDao = mediaDao.mock,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun testFetchMovieDetailsSuccessfully() = runTest {
    val request = MediaRequestApiFactory.movie()

    val expectedResult = movieDetails

    mediaRemote.mockFetchMovieDetails(
      request = request,
      response = flowOf(detailsResponseApi),
    )

    val actualResult = repository.fetchMediaDetails(
      request = MediaRequestApiFactory.movie(),
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test fetch tv details successfully`() = runTest {
    val request = MediaRequestApiFactory.tv()

    JvmUnitTestDemoAssetManager
      .open("details-tv.json")
      .use {
        val json = it.readBytes().decodeToString().trimIndent()
        val serializer = DetailsResponseApi.serializer()
        val tvDetailsResponse = localJson.decodeFromString(serializer, json)

        mediaRemote.mockFetchMovieDetails(
          request = request,
          response = flowOf(tvDetailsResponse),
        )

        repository.fetchMediaDetails(
          request = MediaRequestApiFactory.tv(),
        ).test {
          assertThat(awaitItem()).isEqualTo(
            Result.success(MediaDetailsFactory.TheOffice()),
          )
          awaitComplete()
        }
      }
  }

  @Test
  fun `test fetch details with success also adds media item to database`() = runTest {
    val request = MediaRequestApiFactory.movie()

    mediaRemote.mockFetchMovieDetails(
      request = request,
      response = flowOf(detailsResponseApi),
    )

    repository.fetchMediaDetails(
      request = MediaRequestApiFactory.movie(),
    ).first()

    mediaDao.verifyItemInserted(detailsResponseApi.toDomainMedia().toMediaItem())
  }

  @Test
  fun testFetchMediaReviewsSuccessfully() = runTest {
    val request = MediaRequestApiFactory.movie()

    val expectedResult = expectedReviews

    mediaRemote.mockFetchMovieReviews(
      request = request,
      response = flowOf(reviewsResponseApi),
    )

    val actualResult = repository.fetchMediaReviews(
      request = request,
    ).first()

    assertThat(actualResult.data).isEqualTo(expectedResult)
  }

  @Test
  fun `test fetch movie recommendations with success`() = runTest {
    val request = MediaRequestApiFactory.movie()

    val expectedResult = MediaItemFactory.moviesPagination()

    mediaRemote.mockFetchRecommendedMovies(
      request = request,
      response = flowOf(MoviesResponseApiFactory.full()),
    )

    val actualResult = repository.fetchRecommendedMovies(
      request = request,
    ).first()

    assertThat(actualResult.data).isEqualTo(expectedResult)
  }

  @Test
  fun `test fetch tv recommendations with success`() = runTest {
    val request = MediaRequestApiFactory.tv()

    val expectedResult = MediaItemFactory.tvPagination()

    mediaRemote.mockFetchRecommendedTv(
      request = request,
      response = flowOf(TvResponseApiFactory.full()),
    )

    val actualResult = repository.fetchRecommendedTv(
      request = request,
    ).first()

    assertThat(actualResult.data).isEqualTo(expectedResult)
  }

  @Test
  fun testSimilarMoviesError() = runTest {
    val request = MediaRequestApiFactory.movie()

    val expectedResult = SimilarException()

    repository.fetchRecommendedMovies(
      request = request,
    ).test {
      assertThat(awaitError()).isInstanceOf(expectedResult::class.java)
    }
  }

  @Test
  fun testMovieReviewsError() = runTest {
    val request = MediaRequestApiFactory.movie()

    val expectedResult = ReviewsException()

    repository.fetchMediaReviews(
      request = request,
    ).test {
      assertThat(awaitError()).isInstanceOf(expectedResult::class.java)
    }
  }

  @Test
  fun testMovieDetailsError() = runTest {
    val request = MediaRequestApiFactory.movie()

    val expectedResult = MediaDetailsException()

    repository.fetchMediaDetails(
      request = request,
    ).test {
      assertThat(awaitError()).isInstanceOf(expectedResult::class.java)
    }
  }

  // Movie Videos success
  @Test
  fun testFetchMovieVideosSuccessfully() = runTest {
    val request = MediaRequestApiFactory.movie()

    val expectedResult = listOf(
      Video(
        id = "123",
        key = "123",
        name = "Lorem Ipsum",
        site = VideoSite.YouTube,
        officialTrailer = true,
      ),
      Video(
        id = "1234",
        key = "1234",
        name = "Lorem Ipsum",
        site = VideoSite.Vimeo,
        officialTrailer = false,
      ),
      Video(
        id = "567",
        key = "567",
        name = "Lorem Ipsum",
        site = null,
        officialTrailer = true,
      ),
    )

    mediaRemote.mockFetchMovieVideos(
      request = request,
      response = flowOf(videoResponseApi),
    )

    val actualResult = repository.fetchVideos(
      request = request,
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun testMovieVideosError() = runTest {
    val request = MediaRequestApi.Movie(
      movieId = 555,
    )

    val expectedResult = VideosException()

    repository.fetchVideos(
      request = request,
    ).test {
      assertThat(awaitError()).isInstanceOf(expectedResult::class.java)
    }
  }

  @Test
  fun `test fetch account media details for movie`() = runTest {
    val request = AccountMediaDetailsRequestApi.Movie(
      movieId = 555,
      sessionId = "session_id",
    )

    val response = flowOf(AccountMediaDetailsResponseApiFactory.Rated())
    val expectedResult = AccountMediaDetailsFactory.Rated()

    mediaRemote.mockFetchAccountMediaDetails(
      request = request,
      response = response,
    )

    val actualResult = repository.fetchAccountMediaDetails(
      request = request,
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test add rating for movie`() = runTest {
    val request = AddRatingRequestApi.Movie(
      movieId = 555,
      sessionId = "session_id",
      rating = 5,
    )

    val response = Result.success(
      SubmitOnAccountResponse(
        statusMessage = "Success",
        statusCode = 1,
        success = true,
      ),
    )

    val expectedResult = Unit

    mediaRemote.mockSubmitRating(
      request = request,
      response = response,
    )

    val actualResult = repository.submitRating(
      request = request,
    )

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test delete rating for movie`() = runTest {
    val request = DeleteRatingRequestApi.Movie(
      movieId = 555,
      sessionId = "session_id",
    )

    val response = Result.success(
      SubmitOnAccountResponse(
        statusMessage = "Success",
        statusCode = 1,
        success = true,
      ),
    )

    val expectedResult = Unit

    mediaRemote.mockDeleteRating(
      request = request,
      response = response,
    )

    val actualResult = repository.deleteRating(
      request = request,
    )

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test delete rating for tv`() = runTest {
    val request = DeleteRatingRequestApi.TV(
      seriesId = 555,
      sessionId = "session_id",
    )

    val response = Result.success(
      SubmitOnAccountResponse(
        statusMessage = "Success",
        statusCode = 1,
        success = true,
      ),
    )

    val expectedResult = Unit

    mediaRemote.mockDeleteRating(
      request = request,
      response = response,
    )

    val actualResult = repository.deleteRating(
      request = request,
    )

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test add to watchlist for movie`() = runTest {
    val request = AddToWatchlistRequestApi.Movie(
      movieId = 555,
      accountId = "123456789",
      addToWatchlist = true,
      sessionId = "session_id",
    )

    val response = Result.success(
      SubmitOnAccountResponse(
        statusMessage = "Success",
        statusCode = 1,
        success = true,
      ),
    )

    val expectedResult = Unit

    mediaRemote.mockAddToWatchlist(
      request = request,
      response = response,
    )

    val actualResult = repository.addToWatchlist(
      request = request,
    )

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test fetchCredits when credits exist locally fetches local data`() = runTest {
    creditsDao.mockCheckIfAggregateCreditsExist(true)
    creditsDao.mockFetchAllCredits(AggregateCreditsEntityFactory.theOffice())

    repository.fetchAggregateCredits(1).test {
      val result = awaitItem()
      assertThat(result.isSuccess).isTrue()
      assertThat(result.data).isEqualTo(AggregatedCreditsFactory.partialCredits())
      awaitComplete()
    }
  }

  @Test
  fun `test fetchCredits when credits do not exist locally fetches remote data`() = runTest {
    creditsDao.mockCheckIfAggregateCreditsExist(false)

    mediaRemote.mockFetchAggregateCredits(response = flowOf(creditsResponseApi))

    repository.fetchAggregateCredits(1).test {
      val result = awaitItem()
      assertThat(result.isSuccess).isTrue()
      assertThat(result.data).isEqualTo(AggregatedCreditsFactory.unsortedCredits())
      awaitComplete()
    }
  }

  // Use the DefaultCreditDao instead of the TestCreditDao so that we can actually test the insert
  // of the knownForCredits into the database for this use case.
  @Test
  fun `test fetchCredits when credits do not exist locally inserts data to database`() = runTest {
    val defaultCreditDao = ProdCreditsDao(
      database = TestDatabaseFactory.createInMemoryDatabase(),
      clock = ClockFactory.augustFifteenth2021(),
      dispatcher = testDispatcher,
    )

    repository = ProdDetailsRepository(
      mediaRemote = mediaRemote.mock,
      creditsDao = defaultCreditDao,
      omdbService = omdbService.mock,
      traktService = traktService.mock,
      mediaDao = mediaDao.mock,
      dispatcher = testDispatcher,
    )

    mediaRemote.mockFetchAggregateCredits(response = flowOf(creditsResponseApi))

    repository.fetchAggregateCredits(creditsResponseApi.id).test {
      val existResult = defaultCreditDao.checkIfAggregateCreditsExist(creditsResponseApi.id).first()
      assertThat(existResult).isTrue()

      awaitItem()
      awaitComplete()

      // Re-fetch to ensure that the data is being fetched from the local source
      repository.fetchAggregateCredits(creditsResponseApi.id).test {
        val localResult = defaultCreditDao.fetchAllCredits(creditsResponseApi.id).first()
        assertThat(localResult).isEqualTo(AggregateCreditsEntityFactory.theOffice())
        val result = awaitItem()
        assertThat(result.isSuccess).isTrue()
        assertThat(result.data).isEqualTo(AggregatedCreditsFactory.partialCredits())
      }
    }
  }

  @Test
  fun `test fetch imdb ratings with success`() = runTest {
    val imdbId = "tt0401729"

    omdbService.mockFetchImdbDetails(
      response = OMDbResponseApi(
        metascore = "51",
        imdbRating = "6.6",
        imdbVotes = "289,715",
      ),
    )

    val response = repository.fetchIMDbDetails(
      imdbId = imdbId,
    ).first()

    assertThat(response).isEqualTo(
      Result.success(
        RatingDetails.Score(
          voteAverage = 6.6,
          voteCount = 289_715,
        ),
      ),
    )
  }

  @Test
  fun `test fetch trakt ratings with success`() = runTest {
    traktService.mockFetchRating(
      response = TraktRatingApi(
        rating = 8.5,
        votes = 1_000,
      ),
    )

    val response = repository.fetchTraktRating(
      mediaType = MediaType.MOVIE,
      imdbId = "tt0401729",
    ).first()

    assertThat(response).isEqualTo(
      Result.success(
        RatingDetails.Score(
          voteAverage = 8.5,
          voteCount = 1_000,
        ),
      ),
    )
  }
}
