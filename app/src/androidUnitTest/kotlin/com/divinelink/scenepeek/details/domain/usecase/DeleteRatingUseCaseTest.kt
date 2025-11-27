package com.divinelink.scenepeek.details.domain.usecase

import com.divinelink.core.commons.data
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.divinelink.core.domain.details.media.DeleteRatingParameters
import com.divinelink.core.domain.details.media.DeleteRatingUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class DeleteRatingUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestDetailsRepository

  private lateinit var sessionStorage: SessionStorage

  @Before
  fun setUp() {
    repository = TestDetailsRepository()
  }

  @Test
  fun `test user with no session id cannot submit rating`() = runTest {
    sessionStorage = SessionStorageFactory.empty()

    val useCase = DeleteRatingUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      DeleteRatingParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
      ),
    )

    assertThat(result.isFailure).isTrue()
    assertThat(
      result.exceptionOrNull(),
    ).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `test user with session id can delete rating for movie`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    repository.mockDeleteRating(
      response = Result.success(Unit),
    )

    val useCase = DeleteRatingUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      DeleteRatingParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
      ),
    )

    assertThat(result.isSuccess).isTrue()
    assertThat(result.data).isEqualTo(Unit)
  }

  @Test
  fun `test user with session id can delete rating for tv`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    repository.mockDeleteRating(
      response = Result.success(Unit),
    )

    val useCase = DeleteRatingUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      DeleteRatingParameters(
        id = 0,
        mediaType = MediaType.TV,
      ),
    )

    assertThat(result.isSuccess).isTrue()
    assertThat(result.data).isEqualTo(Unit)
  }

  @Test
  fun `test cannot fetch account media details for unknown media type`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    val useCase = DeleteRatingUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      DeleteRatingParameters(
        id = 0,
        mediaType = MediaType.UNKNOWN,
      ),
    )

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
  }
}
