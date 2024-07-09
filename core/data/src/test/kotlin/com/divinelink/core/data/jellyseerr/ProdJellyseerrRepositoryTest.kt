package com.divinelink.core.data.jellyseerr

import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import com.divinelink.core.network.jellyseerr.model.JellyseerrResponseBodyApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestJellyseerrAccountDetailsQueries
import com.divinelink.core.testing.factories.api.jellyseerr.JellyseerrRequestMediaBodyApiFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.JellyfinLoginResponseApiFactory
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.testing.service.TestJellyseerrService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProdJellyseerrRepositoryTest {

  private lateinit var repository: JellyseerrRepository

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val remote = TestJellyseerrService()
  private val queries = TestJellyseerrAccountDetailsQueries()

  @Before
  fun setUp() {
    repository = ProdJellyseerrRepository(
      service = remote.mock,
      queries = queries.mock,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test sign in with jellyfin successfully`() = runTest {
    remote.mockSignInWithJellyfin(
      response = JellyfinLoginResponseApiFactory.jellyfin(),
    )

    val result = repository.signInWithJellyfin(
      loginData = JellyseerrLoginData(
        username = Username("jellyfinUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
      ),
    )

    assertThat(result.first()).isEqualTo(
      Result.success(JellyseerrAccountDetailsFactory.jellyfin()),
    )
  }

  @Test
  fun `test sign in with jellyseerr successfully`() = runTest {
    remote.mockSignInWithJellyseerr(
      response = JellyfinLoginResponseApiFactory.jellyseerr(),
    )

    val result = repository.signInWithJellyseerr(
      loginData = JellyseerrLoginData(
        username = Username("jellyseerrUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
      ),
    )

    assertThat(result.first()).isEqualTo(
      Result.success(JellyseerrAccountDetailsFactory.jellyseerr()),
    )
  }

  @Test
  fun `test logout successfully`() = runTest {
    remote.mockLogout(
      response = Unit,
    )

    val result = repository.logout(
      address = "http://localhost:8096",
    )

    assertThat(result.first()).isEqualTo(Result.success(Unit))
  }

  @Test
  fun `test tv request media successfully`() = runTest {
    val response = JellyseerrResponseBodyApi(
      message = "Success",
      type = "tv",
    )

    val mappedResponse = JellyseerrMediaRequest(
      message = "Success",
    )

    remote.mockRequestMedia(response = response)

    val result = repository.requestMedia(
      body = JellyseerrRequestMediaBodyApiFactory.tv(),
    )

    assertThat(result.first()).isEqualTo(Result.success(mappedResponse))
  }

  @Test
  fun `test movie request media successfully`() = runTest {
    val response = JellyseerrResponseBodyApi(
      message = "Success",
      type = "movie",
    )

    val mappedResponse = JellyseerrMediaRequest(
      message = "Success",
    )

    remote.mockRequestMedia(response = response)

    val result = repository.requestMedia(
      body = JellyseerrRequestMediaBodyApiFactory.movie(),
    )

    assertThat(result.first()).isEqualTo(Result.success(mappedResponse))
  }
}
