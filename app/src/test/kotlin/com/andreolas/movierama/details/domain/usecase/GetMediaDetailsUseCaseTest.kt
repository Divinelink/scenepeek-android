package com.andreolas.movierama.details.domain.usecase

import app.cash.turbine.test
import com.andreolas.factories.VideoFactory
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.fakes.usecase.details.FakeFetchAccountMediaDetailsUseCase
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.SimilarException
import com.divinelink.core.data.details.model.VideosException
import com.divinelink.core.model.details.DetailsMenuOptions
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
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.divinelink.core.testing.usecase.FakeGetDetailsActionItemsUseCase
import com.divinelink.core.testing.usecase.FakeGetDropdownMenuItemsUseCase
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.divinelink.feature.details.media.usecase.GetMediaDetailsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class GetMediaDetailsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestDetailsRepository
  private lateinit var moviesRepository: FakeMoviesRepository

  private lateinit var fakeFetchAccountMediaDetailsUseCase: FakeFetchAccountMediaDetailsUseCase
  private lateinit var fakeGetDropdownMenuItemsUseCase: FakeGetDropdownMenuItemsUseCase
  private lateinit var fakeGetDetailsActionItemsUseCase: FakeGetDetailsActionItemsUseCase

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
    repository = TestDetailsRepository()
    moviesRepository = FakeMoviesRepository()
    fakeFetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
    fakeGetDropdownMenuItemsUseCase = FakeGetDropdownMenuItemsUseCase()
    fakeGetDetailsActionItemsUseCase = FakeGetDetailsActionItemsUseCase()
  }

  @Test
  fun `test unknown parameters return failure`() = runTest {
    val expectedResult = Result.failure<Exception>(MediaDetailsException())

    val useCase = createGetMediaDetailsUseCase()

    useCase(DetailsRequestApi.Unknown).test {
      assertThat(this.awaitItem().toString()).isEqualTo(expectedResult.toString())
      this.awaitComplete()
    }
  }

  @Test
  fun `successfully get movie details`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(true))
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
    val flow = createGetMediaDetailsUseCase()

    val result = flow(request).first()

    assertThat(result).isEqualTo(
      Result.success(
        MediaDetailsResult.DetailsSuccess(
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
    val flow = createGetMediaDetailsUseCase()
    val result = flow(request).first()

    assertThat(result).isEqualTo(Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)))
  }

  @Test
  fun `successfully fetch movie reviews`() = runTest {
    repository.mockFetchMovieReviews(
      DetailsRequestApi.Movie(movieId = 555),
      Result.success(reviewsList),
    )

    val flow = createGetMediaDetailsUseCase()

    val result = flow(request).last()

    assertThat(result).isEqualTo(Result.success(MediaDetailsResult.ReviewsSuccess(reviewsList)))
  }

  @Test
  fun `successfully fetch similar movies`() = runTest {
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
    repository.mockFetchMovieReviews(request, Result.success(reviewsList))
    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.success(similarList),
    )
    val useCase = createGetMediaDetailsUseCase()

    useCase(request).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.SimilarSuccess(similarList)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.ReviewsSuccess(reviewsList)),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test similar movies are not fetched if details has error`() = runTest {
    repository.mockFetchMovieReviews(
      DetailsRequestApi.Movie(movieId = 555),
      Result.success(reviewsList),
    )
    repository.mockFetchSimilarMovies(
      SimilarRequestApi.Movie(movieId = 555),
      Result.success(similarList),
    )

    val flow = createGetMediaDetailsUseCase()

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

    val useCase = createGetMediaDetailsUseCase()

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

    val useCase = createGetMediaDetailsUseCase()

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
    val flow = createGetMediaDetailsUseCase()

    val result = flow(request).first()

    assertThat(result).isEqualTo(Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)))
  }

  @Test
  fun `successfully get movie details even when reviews and similar calls fail`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(false))
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))

    val flow = createGetMediaDetailsUseCase()

    val result = flow(request).first()

    assertThat(result).isEqualTo(Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)))
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

    val useCase = createGetMediaDetailsUseCase()

    val result = useCase(request).last()

    assertThat(
      result,
    ).isEqualTo(Result.success(MediaDetailsResult.VideosSuccess(videoList.first())))
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

    val useCase = createGetMediaDetailsUseCase()

    val result = useCase(request).last()

    assertThat(result).isEqualTo(Result.success(MediaDetailsResult.VideosSuccess(null)))
  }

  @Test
  fun `test fetch video for tv with success`() = runTest {
    val videoList = VideoFactory.all()
    val tvRequest = DetailsRequestApi.TV(555)

    repository.mockFetchMovieDetails(tvRequest, Result.success(movieDetails))

    repository.mockFetchMovieVideos(tvRequest, Result.success(videoList))

    val useCase = createGetMediaDetailsUseCase()

    useCase(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.VideosSuccess(videoList.first())),
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

    val useCase = createGetMediaDetailsUseCase()

    useCase(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.CreditsSuccess(AggregatedCreditsFactory.credits())),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.VideosSuccess(null)),
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

    val useCase = createGetMediaDetailsUseCase()

    useCase(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.VideosSuccess(trailer = null)),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test aggregate credits are not fetched for movie`() = runTest {
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))
    repository.mockFetchMovieVideos(request, Result.success(emptyList()))

    val useCase = createGetMediaDetailsUseCase()

    useCase(request).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.VideosSuccess(null)),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `catch error case in videos`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Oops."))

    repository.mockFetchMovieVideos(DetailsRequestApi.Movie(555), Result.failure(VideosException()))

    val useCase = createGetMediaDetailsUseCase()

    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `test fetchAccountMediaDetails with success successfully emits data`() = runTest {
    val accountDetails = AccountMediaDetailsFactory.Rated()

    val expectedResult = Result.success(MediaDetailsResult.AccountDetailsSuccess(accountDetails))

    repository.mockFetchMovieDetails(request, Result.success(movieDetails))

    fakeFetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      flowOf(Result.success(accountDetails)),
    )

    val useCase = createGetMediaDetailsUseCase()

    useCase(request).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(awaitItem()).isEqualTo(expectedResult)
      awaitComplete()
    }
  }

  @Test
  fun `test fetchAccountMediaDetails with failure does not emit data`() = runTest {
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))

    fakeFetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      flowOf(Result.failure(Exception("Oops."))),
    )

    val useCase = createGetMediaDetailsUseCase()

    useCase(request).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test getMenuItemsUseCase with success successfully emits data`() = runTest {
    val menuItems = listOf(
      DetailsMenuOptions.SHARE,
    )

    val expectedResult = Result.success(MediaDetailsResult.MenuOptionsSuccess(menuItems))

    repository.mockFetchMovieDetails(request, Result.success(movieDetails))

    fakeGetDropdownMenuItemsUseCase.mockSuccess(flowOf(Result.success(menuItems)))

    val useCase = createGetMediaDetailsUseCase()

    useCase(request).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      assertThat(awaitItem()).isEqualTo(expectedResult)
      awaitComplete()
    }
  }

  @Test
  fun `test getMenuItemsUseCase with failure does not emit data`() = runTest {
    repository.mockFetchMovieDetails(request, Result.success(movieDetails))

    fakeGetDropdownMenuItemsUseCase

    val useCase = createGetMediaDetailsUseCase()

    useCase(request).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.DetailsSuccess(movieDetails)),
      )
      awaitComplete()
    }
  }

  private fun createGetMediaDetailsUseCase() = GetMediaDetailsUseCase(
    repository = repository.mock,
    mediaRepository = moviesRepository.mock,
    dispatcher = testDispatcher,
    fetchAccountMediaDetailsUseCase = fakeFetchAccountMediaDetailsUseCase.mock,
    getMenuItemsUseCase = fakeGetDropdownMenuItemsUseCase.mock,
    getDetailsActionItemsUseCase = fakeGetDetailsActionItemsUseCase.mock,
  )
}
