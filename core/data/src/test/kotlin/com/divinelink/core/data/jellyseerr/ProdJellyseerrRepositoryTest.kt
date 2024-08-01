package com.divinelink.core.data.jellyseerr

import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import com.divinelink.core.database.Database
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import com.divinelink.core.network.jellyseerr.model.JellyseerrResponseBodyApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.JellyseerrRequestMediaBodyApiFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.JellyfinLoginResponseApiFactory
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.testing.service.TestJellyseerrService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class ProdJellyseerrRepositoryTest {

  private lateinit var repository: JellyseerrRepository

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val remote = TestJellyseerrService()

  private lateinit var database: Database

  @Before
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()

    repository = ProdJellyseerrRepository(
      service = remote.mock,
      queries = database.jellyseerrAccountDetailsQueries,
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

  @Test
  fun `test getJellyseerrAccountDetails after insertion`() = runTest {
    val resultNull = repository.getJellyseerrAccountDetails()

    assertThat(resultNull.first()).isNull()

    repository.insertJellyseerrAccountDetails(
      JellyseerrAccountDetailsFactory.jellyfin(),
    )

    val result = repository.getJellyseerrAccountDetails()

    assertThat(result.first()).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin())
  }

  @Test
  fun `test clearJellyseerrAccountDetails`() = runTest {
    repository.insertJellyseerrAccountDetails(
      JellyseerrAccountDetailsFactory.jellyfin(),
    )

    val resultNotNull = repository.getJellyseerrAccountDetails()
    assertThat(resultNotNull.first()).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin())

    repository.clearJellyseerrAccountDetails()

    val result = repository.getJellyseerrAccountDetails()

    assertThat(result.first()).isNull()
  }
}
