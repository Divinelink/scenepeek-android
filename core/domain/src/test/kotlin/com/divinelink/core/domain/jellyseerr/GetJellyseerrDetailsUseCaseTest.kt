package com.divinelink.core.domain.jellyseerr

import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsResultFactory
import com.divinelink.core.network.Resource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.datastore.auth.JellyseerrAccountFactory
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class GetJellyseerrDetailsUseCaseTest {

  private val repository = TestJellyseerrRepository()
  private val authRepository = TestAuthRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test get jellyseerr account details with null storage data`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(null)

    val useCase = GetJellyseerrAccountDetailsUseCase(
      authRepository = authRepository.mock,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(false).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(JellyseerrAccountDetailsResultFactory.initial()),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test get jellyseerr account details with storage data`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(JellyseerrAccountFactory.zabaob())

    repository.mockGetAccountDetails(
      flowOf(Resource.Success(JellyseerrAccountDetailsFactory.jellyseerr())),
    )

    val useCase = GetJellyseerrAccountDetailsUseCase(
      authRepository = authRepository.mock,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(false).first()

    assertThat(result.isSuccess).isTrue()
    assertThat(result.data).isEqualTo(JellyseerrAccountDetailsResultFactory.jellyseerr())
  }

  @Test
  fun `test get remote jellyseerr without active account`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(null)

    repository.mockGetAccountDetails(
      flowOf(Resource.Success(JellyseerrAccountDetailsFactory.jellyseerr())),
    )

    val useCase = GetJellyseerrAccountDetailsUseCase(
      authRepository = authRepository.mock,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(true).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(JellyseerrAccountDetailsResultFactory.initial()),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test get remote jellyseerr account details without local data and address`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(JellyseerrAccountFactory.zabaob())

    repository.mockGetAccountDetails(
      flowOf(
        Resource.Success(null),
        Resource.Success(JellyseerrAccountDetailsFactory.jellyseerr()),
      ),
    )

    val useCase = GetJellyseerrAccountDetailsUseCase(
      authRepository = authRepository.mock,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(true).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(JellyseerrAccountDetailsResultFactory.signedOut()),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(JellyseerrAccountDetailsResultFactory.jellyseerr()),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test get remote jellyseerr account details with failure and no local data`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(JellyseerrAccountFactory.zabaob())

    repository.mockGetAccountDetails(
      flowOf(
        Resource.Success(null),
        Resource.Error(Exception("Error")),
      ),
    )

    val useCase = GetJellyseerrAccountDetailsUseCase(
      authRepository = authRepository.mock,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(true).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(JellyseerrAccountDetailsResultFactory.signedOut()),
      )
      assertThat(awaitItem().getOrNull()).isNull()
      awaitComplete()
    }
  }
}
