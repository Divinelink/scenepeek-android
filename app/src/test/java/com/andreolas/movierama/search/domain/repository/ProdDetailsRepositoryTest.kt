package com.andreolas.movierama.search.domain.repository

import app.cash.turbine.test
import com.andreolas.factories.MediaDetailsFactory
import com.andreolas.factories.MediaItemFactory
import com.andreolas.factories.MediaItemFactory.toWizard
import com.andreolas.factories.ReviewFactory
import com.andreolas.factories.api.DetailsResponseApiFactory
import com.andreolas.factories.api.ReviewsResultsApiFactory
import com.andreolas.factories.api.SimilarMovieApiFactory
import com.andreolas.factories.api.account.states.AccountMediaDetailsResponseApiFactory
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.andreolas.movierama.base.data.remote.movies.dto.account.states.AccountMediaDetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideoResultsApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.rating.AddRatingRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.rating.DeleteRatingRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetailsException
import com.andreolas.movierama.details.domain.model.ReviewsException
import com.andreolas.movierama.details.domain.model.SimilarException
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.domain.model.VideoSite
import com.andreolas.movierama.details.domain.model.VideosException
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import com.andreolas.movierama.details.domain.repository.ProdDetailsRepository
import com.andreolas.movierama.fakes.remote.FakeMovieRemote
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProdDetailsRepositoryTest {

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

  private val similarMovieApiList = SimilarMovieApiFactory.SimilarMovieApiList()

  private val similarResponseApi = SimilarResponseApi(
    page = 1,
    results = similarMovieApiList,
    totalPages = 0,
    totalResults = 0,
  )

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
      )
    )
  )

  private var movieRemote = FakeMovieRemote()

  private lateinit var repository: DetailsRepository

  @Before
  fun setUp() {
    repository = ProdDetailsRepository(
      movieRemote = movieRemote.mock,
    )
  }

  @Test
  fun testFetchMovieDetailsSuccessfully() = runTest {
    val request = DetailsRequestApi.Movie(movieId = 555)

    val expectedResult = movieDetails

    movieRemote.mockFetchMovieDetails(
      request = request,
      response = flowOf(detailsResponseApi)
    )

    val actualResult = repository.fetchMovieDetails(
      request = DetailsRequestApi.Movie(movieId = 555)
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun testFetchMovieReviewsSuccessfully() = runTest {
    val request = ReviewsRequestApi.Movie(movieId = 555)

    val expectedResult = expectedReviews

    movieRemote.mockFetchMovieReviews(
      request = request,
      response = flowOf(reviewsResponseApi)
    )

    val actualResult = repository.fetchMovieReviews(
      request = request
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun testFetchSimilarMoviesSuccessfully() = runTest {
    val request = SimilarRequestApi.Movie(movieId = 555)

    val expectedResult = MediaItemFactory.MoviesList().map { movie ->
      movie.toWizard {
        withPosterPath(posterPath = if (movie.id % 2 == 0) ".jpg" else null)
        withReleaseDate(releaseDate = (2000 + movie.id).toString())
        withName(name = "Lorem Ipsum title")
        withRating(rating = "9.9")
        withOverview(overview = "Lorem Ipsum ${movie.id}")
        withFavorite(null)
      }
    }

    movieRemote.mockFetchSimilarMovies(
      request = request,
      response = flowOf(similarResponseApi)
    )

    val actualResult = repository.fetchSimilarMovies(
      request = request
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun testSimilarMoviesError() = runTest {
    val request = SimilarRequestApi.Movie(movieId = 555)

    val expectedResult = SimilarException()

    repository.fetchSimilarMovies(
      request = request
    ).test {
      assertThat(awaitError()).isInstanceOf(expectedResult::class.java)
    }
  }

  @Test
  fun testMovieReviewsError() = runTest {
    val request = ReviewsRequestApi.Movie(movieId = 555)

    val expectedResult = ReviewsException()

    repository.fetchMovieReviews(
      request = request
    ).test {
      assertThat(awaitError()).isInstanceOf(expectedResult::class.java)
    }
  }

  @Test
  fun testMovieDetailsError() = runTest {
    val request = DetailsRequestApi.Movie(movieId = 555)

    val expectedResult = MovieDetailsException()

    repository.fetchMovieDetails(
      request = request
    ).test {
      assertThat(awaitError()).isInstanceOf(expectedResult::class.java)
    }
  }

  // Movie Videos success
  @Test
  fun testFetchMovieVideosSuccessfully() = runTest {
    val request = VideosRequestApi(
      movieId = 555,
    )
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
      )
    )

    movieRemote.mockFetchMovieVideos(
      request = request,
      response = flowOf(videoResponseApi)
    )

    val actualResult = repository.fetchVideos(
      request = request
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun testMovieVideosError() = runTest {
    val request = VideosRequestApi(
      movieId = 555,
    )

    val expectedResult = VideosException()

    repository.fetchVideos(
      request = request
    ).test {
      assertThat(awaitError()).isInstanceOf(expectedResult::class.java)
    }
  }

  @Test
  fun `test fetch account media details for movie`() = runTest {
    val request = AccountMediaDetailsRequestApi.Movie(
      movieId = 555,
      sessionId = "session_id"
    )

    val response = flowOf(AccountMediaDetailsResponseApiFactory.Rated())
    val expectedResult = AccountMediaDetailsFactory.Rated()

    movieRemote.mockFetchAccountMediaDetails(
      request = request,
      response = response
    )

    val actualResult = repository.fetchAccountMediaDetails(
      request = request
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test add rating for movie`() = runTest {
    val request = AddRatingRequestApi.Movie(
      movieId = 555,
      sessionId = "session_id",
      rating = 5
    )

    val response = flowOf(Unit)
    val expectedResult = Unit

    movieRemote.mockSubmitRating(
      request = request,
      response = response
    )

    val actualResult = repository.submitRating(
      request = request
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test delete rating for movie`() = runTest {
    val request = DeleteRatingRequestApi.Movie(
      movieId = 555,
      sessionId = "session_id",
    )

    val response = flowOf(Unit)
    val expectedResult = Unit

    movieRemote.mockDeleteRating(
      request = request,
      response = response
    )

    val actualResult = repository.deleteRating(
      request = request
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `test delete rating for tv`() = runTest {
    val request = DeleteRatingRequestApi.TV(
      seriesId = 555,
      sessionId = "session_id",
    )

    val response = flowOf(Unit)
    val expectedResult = Unit

    movieRemote.mockDeleteRating(
      request = request,
      response = response
    )

    val actualResult = repository.deleteRating(
      request = request
    ).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }
}
