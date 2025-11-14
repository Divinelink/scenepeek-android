package com.divinelink.core.domain.details.media

import app.cash.turbine.test
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.details.rating.RatingDetailsFactory
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class FetchAllRatingsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestDetailsRepository

  @Before
  fun setUp() {
    repository = TestDetailsRepository()
  }

  @Test
  fun `test fetch all ratings without imdbId sets imdb and trakt to unavailable`() = runTest {
    FetchAllRatingsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      MediaDetailsFactory.FightClub().copy(
        imdbId = null,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(RatingSource.IMDB to RatingDetails.Unavailable),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(RatingSource.TRAKT to RatingDetails.Unavailable),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test fetch all ratings with success from IMDB`() = runTest {
    repository.mockFetchIMDbDetails(
      response = Result.success(RatingDetailsFactory.imdb()),
    )

    repository.mockFetchTraktRating(
      response = Result.failure(Exception()),
    )

    FetchAllRatingsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(MediaDetailsFactory.FightClub()).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(RatingSource.IMDB to RatingDetailsFactory.imdb()),
      )
      awaitItem()
      awaitComplete()
    }
  }

  @Test
  fun `test fetch all ratings with success from trakt`() = runTest {
    repository.mockFetchIMDbDetails(response = Result.failure(Exception()))

    repository.mockFetchTraktRating(response = Result.success(RatingDetailsFactory.trakt()))

    FetchAllRatingsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(MediaDetailsFactory.FightClub()).test {
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(Exception())::class.java,
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(RatingSource.TRAKT to RatingDetailsFactory.trakt()),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test fetch all ratings with imdb and trakt success`() = runTest {
    repository.mockFetchIMDbDetails(response = Result.success(RatingDetailsFactory.imdb()))
    repository.mockFetchTraktRating(response = Result.success(RatingDetailsFactory.trakt()))

    FetchAllRatingsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(MediaDetailsFactory.FightClub()).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(RatingSource.IMDB to RatingDetailsFactory.imdb()),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(RatingSource.TRAKT to RatingDetailsFactory.trakt()),
      )
      awaitComplete()
    }
  }
}
