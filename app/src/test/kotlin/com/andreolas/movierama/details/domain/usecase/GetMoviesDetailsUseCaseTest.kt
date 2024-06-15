package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
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
import com.divinelink.core.network.media.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.media.model.details.videos.VideosRequestApi
import com.divinelink.feature.details.ui.MovieDetailsResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
      date = "2022-10-10"
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
  fun `successfully get movie details`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(true))
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
//    repository.mockFetchMovieReviews(ReviewsRequestApi.Movie(555), Result.failure<Exception>())
//    repository.mockFetchSimilarMovies(SimilarRequestApi.Movie(mediaId = 555), Result.Loading)
    val flow = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).first()

    assertThat(result).isEqualTo(
      Result.success(
        MovieDetailsResult.DetailsSuccess(
          movieDetails.copy(
            isFavorite = true
          )
        )
      )
    )
  }

  @Test
  fun `successfully get movie details with false favorite status`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(false))
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
//    repository.mockFetchMovieReviews(ReviewsRequestApi.Movie(555), Result.Loading)
//    repository.mockFetchSimilarMovies(SimilarRequestApi.Movie(mediaId = 555), Result.Loading)
    val flow = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
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
      ReviewsRequestApi.Movie(movieId = 555),
      Result.success(reviewsList)
    )
//    repository.mockFetchSimilarMovies(SimilarRequestApi.Movie(mediaId = 555), Result.Loading)
    val flow = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).last()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)))
  }

  @Test
  fun `successfully fetch similar movies`() = runTest {
//    repository.mockFetchMovieDetails(request, Result.Loading)
    repository.mockFetchMovieReviews(
      ReviewsRequestApi.Movie(movieId = 555),
      Result.success(reviewsList)
    )
    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.success(similarList)
    )
    val flow = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).last()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.SimilarSuccess(similarList)))
  }

  @Test
  fun `given error result, I expect error result`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Oops."))

    repository.mockFetchMovieDetails(
      request = request,
      response = Result.failure(Exception("Oops."))
    )

    val useCase = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
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

//    repository.mockFetchMovieReviews(ReviewsRequestApi.Movie(555), Result.Loading)
    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.failure(SimilarException())
    )

    val useCase = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
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
//    repository.mockFetchMovieReviews(ReviewsRequestApi.Movie(555), Result.Loading)
    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.failure(SimilarException())
    )
    val flow = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
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
    val flow = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
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
      )
    )
    repository.mockFetchMovieVideos(VideosRequestApi(555), Result.success(videoList))
    val flow = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).last()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.VideosSuccess(videoList.first())))
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
      )
    )
    repository.mockFetchMovieVideos(VideosRequestApi(555), Result.success(videoList))
    val flow = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = flow(request).last()

    assertThat(result).isEqualTo(Result.success(MovieDetailsResult.VideosSuccess(null)))
  }

  @Test
  fun `catch error case in videos`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Oops."))

    repository.mockFetchMovieVideos(VideosRequestApi(555), Result.failure(VideosException()))

    val useCase = com.divinelink.feature.details.usecase.GetMovieDetailsUseCase(
      repository = repository.mock,
      mediaRepository = moviesRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
