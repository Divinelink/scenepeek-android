package com.divinelink.scenepeek.details.domain.usecase

import app.cash.turbine.test
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.RecommendedException
import com.divinelink.core.data.details.model.VideosException
import com.divinelink.core.fixtures.details.review.ReviewFactory
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.details.rating.RatingDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.api.media.MediaRequestApiFactory
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.core.testing.usecase.FakeGetDetailsActionItemsUseCase
import com.divinelink.core.testing.usecase.FakeGetDropdownMenuItemsUseCase
import com.divinelink.factories.VideoFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.divinelink.feature.details.media.usecase.GetMediaDetailsUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeFetchAccountMediaDetailsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
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
  private lateinit var moviesRepository: TestMediaRepository
  private lateinit var jellyseerrRepository: TestJellyseerrRepository

  private lateinit var fakeFetchAccountMediaDetailsUseCase: FakeFetchAccountMediaDetailsUseCase
  private lateinit var fakeGetDropdownMenuItemsUseCase: FakeGetDropdownMenuItemsUseCase
  private lateinit var fakeGetDetailsActionItemsUseCase: FakeGetDetailsActionItemsUseCase
  private lateinit var preferenceStorage: FakePreferenceStorage

  private val movieRequest = MediaRequestApiFactory.movie()
  private val tvRequest = MediaRequestApiFactory.tv()
  private val movieDetails = MediaDetailsFactory.FightClub()
  private val tvDetails = MediaDetailsFactory.TheOffice()

  @Before
  fun setUp() {
    repository = TestDetailsRepository()
    moviesRepository = TestMediaRepository()
    jellyseerrRepository = TestJellyseerrRepository()
    fakeFetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
    fakeGetDropdownMenuItemsUseCase = FakeGetDropdownMenuItemsUseCase()
    fakeGetDetailsActionItemsUseCase = FakeGetDetailsActionItemsUseCase()
    preferenceStorage = FakePreferenceStorage()
  }

  @Test
  fun `test unknown parameters return failure`() = runTest {
    val expectedResult = Result.failure<Exception>(MediaDetailsException())

    val useCase = createGetMediaDetailsUseCase()

    useCase(MediaRequestApi.Unknown).test {
      assertThat(this.awaitItem().toString()).isEqualTo(expectedResult.toString())
      this.awaitComplete()
    }
  }

  @Test
  fun `successfully get movie details`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(true))
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    val flow = createGetMediaDetailsUseCase()

    val result = flow(movieRequest).first()

    assertThat(result).isEqualTo(
      Result.success(
        MediaDetailsResult.DetailsSuccess(
          mediaDetails = movieDetails.copy(
            isFavorite = true,
          ),
          ratingSource = RatingSource.TMDB,
        ),
      ),
    )
  }

  @Test
  fun `test get movie details with Trakt rating source`() = runTest {
    preferenceStorage.setMovieRatingSource(RatingSource.TRAKT)

    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(true))
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    repository.mockFetchTraktRating(
      response = Result.success(
        RatingDetails.Score(
          voteAverage = 7.3,
          voteCount = 1_234,
        ),
      ),
    )
    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails.copy(isFavorite = true),
            ratingSource = RatingSource.TRAKT,
          ),
        ),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.RatingSuccess(
            RatingCount(
              ratings = mapOf(
                RatingSource.TMDB to RatingDetails.Score(
                  voteAverage = 8.4,
                  voteCount = 30_452,
                ),
                RatingSource.IMDB to RatingDetails.Initial,
                RatingSource.TRAKT to RatingDetails.Score(
                  voteAverage = 7.3,
                  voteCount = 1_234,
                ),
              ),
            ),
          ),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test get movie details with Trakt rating source with error response`() = runTest {
    preferenceStorage.setMovieRatingSource(RatingSource.TRAKT)

    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(true))
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails.copy(isFavorite = true),
            ratingSource = RatingSource.TRAKT,
          ),
        ),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.RatingSuccess(
            RatingCount(
              ratings = mapOf(
                RatingSource.TMDB to RatingDetails.Score(
                  voteAverage = 8.4,
                  voteCount = 30_452,
                ),
                RatingSource.IMDB to RatingDetails.Initial,
                RatingSource.TRAKT to RatingDetails.Unavailable,
              ),
            ),
          ),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test get movie details with IMDB rating source`() = runTest {
    preferenceStorage.setMovieRatingSource(RatingSource.IMDB)

    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(true))
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    repository.mockFetchIMDbDetails(
      response = Result.success(
        RatingDetails.Score(
          voteAverage = 8.7,
          voteCount = 4_567,
        ),
      ),
    )
    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails.copy(isFavorite = true),
            ratingSource = RatingSource.IMDB,
          ),
        ),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.RatingSuccess(
            RatingCount(
              ratings = mapOf(
                RatingSource.TMDB to RatingDetails.Score(
                  voteAverage = 8.4,
                  voteCount = 30_452,
                ),
                RatingSource.IMDB to RatingDetails.Score(
                  voteAverage = 8.7,
                  voteCount = 4_567,
                ),
                RatingSource.TRAKT to RatingDetails.Initial,
              ),
            ),
          ),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test get movie details with IMDB rating source with failed response`() = runTest {
    preferenceStorage.setMovieRatingSource(RatingSource.IMDB)

    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(true))
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails.copy(isFavorite = true),
            ratingSource = RatingSource.IMDB,
          ),
        ),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.RatingSuccess(
            RatingCount(
              ratings = mapOf(
                RatingSource.TMDB to RatingDetails.Score(
                  voteAverage = 8.4,
                  voteCount = 30_452,
                ),
                RatingSource.IMDB to RatingDetails.Unavailable,
                RatingSource.TRAKT to RatingDetails.Initial,
              ),
            ),
          ),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test get tv details with Trakt rating source with success response`() = runTest {
    preferenceStorage.setMovieRatingSource(RatingSource.IMDB)
    preferenceStorage.setTvRatingSource(RatingSource.TRAKT)

    val tvRequest = MediaRequestApi.TV(555)

    repository.mockFetchMediaDetails(tvRequest, Result.success(tvDetails))

    repository.mockFetchTraktRating(Result.success(RatingDetailsFactory.trakt()))
    val useCase = createGetMediaDetailsUseCase()

    useCase(tvRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = tvDetails,
            ratingSource = RatingSource.TRAKT,
          ),
        ),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.RatingSuccess(
            RatingCount(
              ratings = mapOf(
                RatingSource.TMDB to RatingDetails.Score(8.6, 4503),
                RatingSource.IMDB to RatingDetails.Initial,
                RatingSource.TRAKT to RatingDetailsFactory.trakt(),
              ),
            ),
          ),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `successfully get movie details with false favorite status`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(false))
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    val flow = createGetMediaDetailsUseCase()
    val result = flow(movieRequest).first()

    assertThat(result).isEqualTo(
      Result.success(
        MediaDetailsResult.DetailsSuccess(
          mediaDetails = movieDetails,
          ratingSource = RatingSource.TMDB,
        ),
      ),
    )
  }

  @Test
  fun `successfully fetch movie reviews`() = runTest {
    repository.mockFetchMovieReviews(
      request = MediaRequestApiFactory.movie(),
      response = Result.success(ReviewFactory.ReviewList()),
    )

    val flow = createGetMediaDetailsUseCase()

    val result = flow(movieRequest).last()

    assertThat(
      result,
    ).isEqualTo(
      Result.success(
        MediaDetailsResult.ReviewsSuccess(
          formOrder = MovieTab.Reviews.order,
          reviews = ReviewFactory.ReviewList(),
        ),
      ),
    )
  }

  @Test
  fun `successfully fetch similar movies`() = runTest {
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    repository.mockFetchMovieReviews(movieRequest, Result.success(ReviewFactory.ReviewList()))
    repository.mockFetchSimilarMovies(
      MediaRequestApiFactory.movie(),
      Result.success(MediaItemFactory.moviesPagination()),
    )
    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.RecommendedSuccess(
            formOrder = MovieTab.Recommendations.order,
            similar = MediaItemFactory.moviesPagination().list,
          ),
        ),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.ReviewsSuccess(
            formOrder = MovieTab.Reviews.order,
            reviews = ReviewFactory.ReviewList(),
          ),
        ),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test similar movies are not fetched if details has error`() = runTest {
    repository.mockFetchMovieReviews(
      MediaRequestApiFactory.movie(),
      Result.success(ReviewFactory.ReviewList()),
    )
    repository.mockFetchSimilarMovies(
      MediaRequestApiFactory.movie(),
      Result.success(MediaItemFactory.moviesPagination()),
    )

    val flow = createGetMediaDetailsUseCase()

    val result = flow(movieRequest).first()

    assertThat(result.toString()).isEqualTo(
      Result.failure<Throwable>(MediaDetailsException()).toString(),
    )
  }

  @Test
  fun `given error result, I expect error result`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Oops."))

    repository.mockFetchMediaDetails(
      request = movieRequest,
      response = Result.failure(Exception("Oops.")),
    )

    val useCase = createGetMediaDetailsUseCase()

    val result = useCase(movieRequest).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `catch error case in details`() = runTest {
    val expectedResult = Result.failure<Exception>(MediaDetailsException())

    repository.mockFetchSimilarMovies(
      MediaRequestApiFactory.movie(),
      Result.failure(RecommendedException(MovieTab.Recommendations.order)),
    )

    val useCase = createGetMediaDetailsUseCase()

    val result = useCase(movieRequest).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `successfully get movie details even when similar call fails`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(false))
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    repository.mockFetchSimilarMovies(
      MediaRequestApiFactory.movie(),
      Result.failure(RecommendedException(MovieTab.Recommendations.order)),
    )
    val flow = createGetMediaDetailsUseCase()

    val result = flow(movieRequest).first()

    assertThat(result).isEqualTo(
      Result.success(
        MediaDetailsResult.DetailsSuccess(
          mediaDetails = movieDetails,
          ratingSource = RatingSource.TMDB,
        ),
      ),
    )
  }

  @Test
  fun `successfully get movie details even when reviews and similar calls fail`() = runTest {
    moviesRepository.mockCheckFavorite(555, MediaType.MOVIE, Result.success(false))
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))

    val flow = createGetMediaDetailsUseCase()

    val result = flow(movieRequest).first()

    assertThat(result).isEqualTo(
      Result.success(
        MediaDetailsResult.DetailsSuccess(
          mediaDetails = movieDetails,
          ratingSource = RatingSource.TMDB,
        ),
      ),
    )
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
    repository.mockFetchMovieVideos(MediaRequestApiFactory.movie(), Result.success(videoList))

    val useCase = createGetMediaDetailsUseCase()

    val result = useCase(movieRequest).last()

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
    repository.mockFetchMovieVideos(MediaRequestApiFactory.movie(), Result.success(videoList))

    val useCase = createGetMediaDetailsUseCase()

    val result = useCase(movieRequest).last()

    assertThat(result).isEqualTo(Result.success(MediaDetailsResult.VideosSuccess(null)))
  }

  @Test
  fun `test fetch video for tv with success`() = runTest {
    val videoList = VideoFactory.all()
    val tvRequest = MediaRequestApiFactory.tv()

    repository.mockFetchMediaDetails(tvRequest, Result.success(movieDetails))

    repository.mockFetchMovieVideos(tvRequest, Result.success(videoList))

    val useCase = createGetMediaDetailsUseCase()

    useCase(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.VideosSuccess(VideoFactory.Youtube())),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test aggregate credits are fetched for tv`() = runTest {
    val tvRequest = MediaRequestApiFactory.tv()
    repository.mockFetchMediaDetails(tvRequest, Result.success(movieDetails))
    repository.mockFetchMovieVideos(tvRequest, Result.success(emptyList()))
    repository.mockFetchAggregateCredits(Result.success(AggregatedCreditsFactory.credits()))

    val useCase = createGetMediaDetailsUseCase()

    useCase(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
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
    val tvRequest = MediaRequestApiFactory.tv()
    repository.mockFetchMediaDetails(tvRequest, Result.success(movieDetails))
    repository.mockFetchMovieVideos(tvRequest, Result.success(emptyList()))
    repository.mockFetchAggregateCredits(Result.failure(Exception()))

    val useCase = createGetMediaDetailsUseCase()

    useCase(tvRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(MediaDetailsResult.VideosSuccess(trailer = null)),
      )
      this.awaitComplete()
    }
  }

  @Test
  fun `test aggregate credits are not fetched for movie`() = runTest {
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))
    repository.mockFetchMovieVideos(movieRequest, Result.success(emptyList()))

    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(this.awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
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

    repository.mockFetchMovieVideos(
      MediaRequestApiFactory.movie(),
      Result.failure(VideosException()),
    )

    val useCase = createGetMediaDetailsUseCase()

    val result = useCase(movieRequest).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `test fetchAccountMediaDetails with success successfully emits data`() = runTest {
    val accountDetails = AccountMediaDetailsFactory.Rated()

    val expectedResult = Result.success(MediaDetailsResult.AccountDetailsSuccess(accountDetails))

    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))

    fakeFetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      flowOf(Result.success(accountDetails)),
    )

    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      assertThat(awaitItem()).isEqualTo(expectedResult)
      awaitComplete()
    }
  }

  @Test
  fun `test fetchAccountMediaDetails with failure does not emit data`() = runTest {
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))

    fakeFetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      flowOf(Result.failure(Exception("Oops."))),
    )

    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
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

    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))

    fakeGetDropdownMenuItemsUseCase.mockSuccess(flowOf(Result.success(menuItems)))

    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      assertThat(awaitItem()).isEqualTo(expectedResult)
      awaitComplete()
    }
  }

  @Test
  fun `test getMenuItemsUseCase with failure does not emit data`() = runTest {
    repository.mockFetchMediaDetails(movieRequest, Result.success(movieDetails))

    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test jellyseerr getMovieDetails with success awaits for details to respond`() = runTest {
    val channel = Channel<Result<MediaDetails>>()

    repository.mockFetchMediaDetails(channel)
    jellyseerrRepository.mockGetMovieDetails(JellyseerrMediaInfoFactory.Movie.pending())

    val useCase = createGetMediaDetailsUseCase()

    useCase(movieRequest).test {
      expectNoEvents()
      channel.trySend(Result.success(movieDetails))

      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )

      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.JellyseerrDetailsSuccess(
            info = JellyseerrMediaInfoFactory.Movie.pending(),
          ),
        ),
      )
    }
  }

  @Test
  fun `test jellyseerr getTvDetails with success also awaits for details to respond`() = runTest {
    val channel = Channel<Result<MediaDetails>>()

    repository.mockFetchMediaDetails(channel)
    jellyseerrRepository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.available())

    val useCase = createGetMediaDetailsUseCase()

    useCase(tvRequest).test {
      expectNoEvents()
      channel.trySend(Result.success(tvDetails))

      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = tvDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )

      assertThat(awaitItem()).isEqualTo(
        Result.success(
          MediaDetailsResult.JellyseerrDetailsSuccess(
            info = JellyseerrMediaInfoFactory.Tv.available(),
          ),
        ),
      )
    }
  }

  private fun createGetMediaDetailsUseCase() = GetMediaDetailsUseCase(
    repository = repository.mock,
    mediaRepository = moviesRepository.mock,
    jellyseerrRepository = jellyseerrRepository.mock,
    dispatcher = testDispatcher,
    fetchAccountMediaDetailsUseCase = fakeFetchAccountMediaDetailsUseCase.mock,
    getMenuItemsUseCase = fakeGetDropdownMenuItemsUseCase.mock,
    getDetailsActionItemsUseCase = fakeGetDetailsActionItemsUseCase.mock,
    preferenceStorage = preferenceStorage,
  )
}
