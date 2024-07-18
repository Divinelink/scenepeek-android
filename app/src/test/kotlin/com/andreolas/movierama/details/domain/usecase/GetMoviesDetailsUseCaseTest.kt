package com.andreolas.movierama.details.domain.usecase

import app.cash.turbine.test
import com.andreolas.factories.VideoFactory
import com.andreolas.movierama.fakes.repository.FakeDetailsRepository
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.SimilarException
import com.divinelink.core.data.details.model.VideosException
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.feature.details.ui.MovieDetailsResult
import com.divinelink.feature.details.usecase.GetMovieDetailsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetMoviesDetailsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: FakeDetailsRepository
  private lateinit var moviesRepository: FakeMoviesRepository

  private val request = DetailsRequestApi.Movie(movieId = 555)
  private val movieDetails = Movie(
    id = 0,
    title = "",
    posterPath = "",
    overview = null,
    director = null,
    genres = listOf(),
    cast = listOf(),
    releaseDate = "",
    rating = "",
    isFavorite = false,
    runtime = "50m",
  )

  private val reviewsList = (1..10).map {
    Review(
      authorName = "Lorem Ipsum name $it",
      rating = it,
      content = "Lorame Ipsum content $it",
      date = "2022-10-10",
    )
  }.toList()

  private val similarList = (1..10).map {
    MediaItem.Media.Movie(
      id = it,
      name = "",
      posterPath = null,
      releaseDate = "",
      rating = "",
      overview = "",
      isFavorite = null,
    )
  }.toList()

  @Before
  fun setUp() {
    repository = FakeDetailsRepository()
    moviesRepository = FakeMoviesRepository()
  }

  @Test
  fun `test unknown parameters return failure`() = runTest {
    val expectedResult = Result.failure<Exception>(MediaDetailsException())

    val useCase = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase(DetailsRequestApi.Unknown).test {
      assertThat(this.awaitItem().toString()).isEqualTo(expectedResult.toString())
      this.awaitComplete()
    }
  }

  @Test
  fun `successfully get movie details`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(true))
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
//    repository.mockFetchMovieReviews(ReviewsRequestApi.Movie(555), Result.failure<Exception>())
//    repository.mockFetchSimilarMovies(SimilarRequestApi.Movie(mediaId = 555), Result.Loading)
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).first()

    assertThat(result).isEqualTo(
      Result.success(
        MovieDetailsResult.DetailsSuccess(
          movieDetails.copy(
            isFavorite = true,
          ),
        ),
      ),
    )
  }

  @Test
  fun `successfully get movie details with false favorite status`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(false))
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
//    repository.mockFetchMovieReviews(ReviewsRequestApi.Movie(555), Result.Loading)
//    repository.mockFetchSimilarMovies(SimilarRequestApi.Movie(mediaId = 555), Result.Loading)
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).first()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
  }

  @Test
  fun `successfully fetch movie reviews`() = runTest {
//    repository.mockFetchMovieDetails(request, Result.Loading)
    repository.mockFetchMovieReviews(
      DetailsRequestApi.Movie(movieId = 555),
      Result.success(reviewsList),
    )
//    repository.mockFetchSimilarMovies(SimilarRequestApi.Movie(mediaId = 555), Result.Loading)
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).last()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)))
  }

  @Test
  fun `successfully fetch similar movies`() = runTest {
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
    repository.mockFetchMovieReviews(request, Result.success(reviewsList))
    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.success(similarList),
    )
    val useCase = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase(request).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.SimilarSuccess(similarList)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test similar movies are not fetched is details has error`() = runTest {
    repository.mockFetchMovieReviews(
      DetailsRequestApi.Movie(movieId = 555),
      Result.success(reviewsList),
    )
    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.success(similarList),
    )
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).first()

    assertThat(result.toString()).isEqualTo(
      Result.failure<Throwable>(MediaDetailsException()).toString(),
    )
  }

  @Test
  fun `given error result, I expect error result`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Oops."))

    repository.mockFetchMovieDetails(
      request = request,
      response = Result.failure(Exception("Oops.")),
    )

    val useCase = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `catch error case in details`() = runTest {
    val expectedResult = Result.failure<Exception>(MediaDetailsException())

    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.failure(SimilarException()),
    )

    val useCase = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `successfully get movie details even when similar call fails`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(false))
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.failure(SimilarException()),
    )
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).first()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
  }

  @Test
  fun `successfully get movie details even when reviews and similar calls fail`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(false))
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).first()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
  }

  // Video tests
  @Test
  fun `successfully get movie videos`() = runTest {
    val videoList = listOf(
      Video(
        id = "1",
        name = "key",
        officialTrailer = true,
        site = VideoSite.YouTube,
        key = "type",
      ),
    )
    repository.mockFetchMovieVideos(DetailsRequestApi.Movie(555), Result.success(videoList))
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).last()

    assertThat(
      result,
    ).isEqualTo(Result.success(MovieDetailsResult.VideosSuccess(videoList.first())))
  }

  @Test
  fun `successfully get movie videos with empty list`() = runTest {
    val videoList = listOf(
      Video(
        id = "1",
        name = "key",
        officialTrailer = false,
        site = VideoSite.Vimeo,
        key = "type",
      ),
    )
    repository.mockFetchMovieVideos(DetailsRequestApi.Movie(555), Result.success(videoList))
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).last()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.VideosSuccess(null)))
  }

  @Test
  fun `test fetch video for tv with success`() = runTest {
    val videoList = VideoFactory.all()
    val tvRequest = DetailsRequestApi.TV(555)

    repository.mockFetchMovieDetails(tvRequest, Result.success(movieDetails))

    repository.mockFetchMovieVideos(tvRequest, Result.success(videoList))
    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    flow(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.VideosSuccess(videoList.first())),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test aggregate credits are fetched for tv`() = runTest {
    val tvRequest = DetailsRequestApi.TV(555)
    repository.mockFetchMovieDetails(tvRequest, Result.success(movieDetails))
    repository.mockFetchMovieVideos(tvRequest, Result.success(emptyList()))
    repository.mockFetchAggregateCredits(Result.success(AggregatedCreditsFactory.credits()))

    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    flow(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.CreditsSuccess(AggregatedCreditsFactory.credits())),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.VideosSuccess(null)),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test aggregate credits with failure for tv`() = runTest {
    val tvRequest = DetailsRequestApi.TV(555)
    repository.mockFetchMovieDetails(tvRequest, Result.success(movieDetails))
    repository.mockFetchMovieVideos(tvRequest, Result.success(emptyList()))
    repository.mockFetchAggregateCredits(Result.failure(Exception()))

    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    flow(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.VideosSuccess(trailer = null)),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test aggregate credits are not fetched for movie`() = runTest {
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
    repository.mockFetchMovieVideos(request, Result.success(emptyList()))

    val flow = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    flow(request).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MovieDetailsResult.VideosSuccess(null)),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `catch error case in videos`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Oops."))

    repository.mockFetchMovieVideos(DetailsRequestApi.Movie(555), Result.failure(VideosException()))

    val useCase = GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
