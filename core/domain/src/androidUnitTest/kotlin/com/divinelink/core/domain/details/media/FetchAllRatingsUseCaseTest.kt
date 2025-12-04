package com.divinelink.core.domain.details.media

import app.cash.turbine.test
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.details.rating.ExternalRatingsFactory
import com.divinelink.core.fixtures.model.details.rating.RatingDetailsFactory
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestDetailsRepository
import io.kotest.matchers.shouldBe
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
  fun `test fetch all ratings without imdbId sets all ratings to unavailable`() = runTest {
    FetchAllRatingsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      MediaDetailsFactory.FightClub().copy(
        imdbId = null,
      ),
    ).test {
      awaitItem() shouldBe Result.success(RatingSource.IMDB to RatingDetails.Unavailable)
      awaitItem() shouldBe Result.success(RatingSource.TRAKT to RatingDetails.Unavailable)
      awaitItem() shouldBe Result.success(RatingSource.RT to RatingDetails.Unavailable)
      awaitItem() shouldBe Result.success(RatingSource.METACRITIC to RatingDetails.Unavailable)
      awaitComplete()
    }
  }

  @Test
  fun `test fetch all ratings with success from IMDB`() = runTest {
    repository.mockFetchExternalRatings(
      response = Result.success(ExternalRatingsFactory.all),
    )

    repository.mockFetchTraktRating(
      response = Result.failure(Exception()),
    )

    FetchAllRatingsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(MediaDetailsFactory.FightClub()).test {
      awaitItem() shouldBe Result.success(RatingSource.IMDB to RatingDetailsFactory.imdb())
      awaitItem() shouldBe Result.success(
        RatingSource.METACRITIC to RatingDetailsFactory.metacritic,
      )
      awaitItem() shouldBe Result.success(RatingSource.RT to RatingDetailsFactory.rottenTomatoes)
      awaitItem()
      awaitComplete()
    }
  }

  @Test
  fun `test fetch all ratings with success from trakt`() = runTest {
    repository.mockFetchExternalRatings(response = Result.failure(Exception()))

    repository.mockFetchTraktRating(response = Result.success(RatingDetailsFactory.trakt()))

    FetchAllRatingsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(MediaDetailsFactory.FightClub()).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(Exception()).toString()
      awaitItem() shouldBe Result.success(RatingSource.TRAKT to RatingDetailsFactory.trakt())
      awaitComplete()
    }
  }

  @Test
  fun `test fetch all ratings with imdb and trakt success`() = runTest {
    repository.mockFetchExternalRatings(response = Result.success(ExternalRatingsFactory.all))
    repository.mockFetchTraktRating(response = Result.success(RatingDetailsFactory.trakt()))

    FetchAllRatingsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(MediaDetailsFactory.FightClub()).test {
      awaitItem() shouldBe Result.success(RatingSource.IMDB to RatingDetailsFactory.imdb())
      awaitItem() shouldBe Result.success(
        RatingSource.METACRITIC to RatingDetailsFactory.metacritic,
      )
      awaitItem() shouldBe Result.success(RatingSource.RT to RatingDetailsFactory.rottenTomatoes)
      awaitItem() shouldBe Result.success(RatingSource.TRAKT to RatingDetailsFactory.trakt())
      awaitComplete()
    }
  }
}
