package com.divinelink.core.data.jellyseerr

import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import com.divinelink.core.database.Database
import com.divinelink.core.fixtures.core.network.jellyseerr.model.JellyseerrRequestMediaResponseFactory
import com.divinelink.core.fixtures.core.network.jellyseerr.model.movie.MovieInfoResponseFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.fixtures.model.jellyseerr.request.JellyseerrMediaRequestResponseFactory
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.exception.MissingJellyseerrHostAddressException
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequester
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.divinelink.core.network.jellyseerr.model.RequestedByResponse
import com.divinelink.core.network.jellyseerr.model.movie.JellyseerrMovieDetailsResponse
import com.divinelink.core.network.jellyseerr.model.movie.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.tv.JellyseerrTvDetailsResponse
import com.divinelink.core.network.jellyseerr.model.tv.TvInfoResponse
import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.JellyseerrRequestMediaBodyApiFactory
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
    remote.mockSignInWithJellyfin(response = Unit)

    val result = repository.signInWithJellyfin(
      loginData = JellyseerrLoginData(
        username = Username("jellyfinUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
      ),
    )

    assertThat(result.first()).isEqualTo(Result.success(Unit))
  }

  @Test
  fun `test sign in with jellyseerr successfully`() = runTest {
    remote.mockSignInWithJellyseerr(response = Unit)

    val result = repository.signInWithJellyseerr(
      loginData = JellyseerrLoginData(
        username = Username("jellyseerrUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
      ),
    )

    assertThat(result.first()).isEqualTo(Result.success(Unit))
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
    val response = JellyseerrRequestMediaResponseFactory.tv()

    val mappedResponse = JellyseerrMediaRequestResponseFactory.tvPartially()

    remote.mockRequestMedia(response = response)

    val result = repository.requestMedia(
      body = JellyseerrRequestMediaBodyApiFactory.tv(),
    )

    assertThat(result.first()).isEqualTo(Result.success(mappedResponse))
  }

  @Test
  fun `test movie request media successfully`() = runTest {
    val response = JellyseerrRequestMediaResponseFactory.movie(
      status = JellyseerrMediaStatus.PROCESSING,
    )

    val mappedResponse = JellyseerrMediaRequestResponseFactory.movie().copy(
      mediaInfo = JellyseerrMediaInfoFactory.Movie.processing(),
    )

    remote.mockRequestMedia(response = response)

    val result = repository.requestMedia(
      body = JellyseerrRequestMediaBodyApiFactory.movie(),
    )

    assertThat(result.first()).isEqualTo(Result.success(mappedResponse))
  }

  @Test
  fun `test getRemoteJellyseerrAccountDetails after insertion`() = runTest {
    val domain = JellyseerrAccountDetailsFactory.jellyfin()

    remote.mockFetchAccountDetails(
      JellyseerrAccountDetailsResponseApi(
        id = domain.id,
        email = domain.email!!,
        displayName = domain.displayName,
        avatar = "/avatarproxy/1dde62cf4a2c436d95e17b9",
        requestCount = domain.requestCount,
        createdAt = domain.createdAt,
      ),
    )

    val result = repository.getRemoteAccountDetails("http://localhost:5000")

    assertThat(result.first()).isEqualTo(Result.success(JellyseerrAccountDetailsFactory.jellyfin()))
  }

  @Test
  fun `test getLocalJellyseerrAccountDetails after insertion`() = runTest {
    val resultNull = repository.getLocalJellyseerrAccountDetails()

    assertThat(resultNull.first()).isNull()

    repository.insertJellyseerrAccountDetails(
      JellyseerrAccountDetailsFactory.jellyfin(),
    )

    val result = repository.getLocalJellyseerrAccountDetails()

    assertThat(result.first()).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin())
  }

  @Test
  fun `test clearJellyseerrAccountDetails`() = runTest {
    repository.insertJellyseerrAccountDetails(
      JellyseerrAccountDetailsFactory.jellyfin(),
    )

    val resultNotNull = repository.getLocalJellyseerrAccountDetails()
    assertThat(resultNotNull.first()).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin())

    repository.clearJellyseerrAccountDetails()

    val result = repository.getLocalJellyseerrAccountDetails()

    assertThat(result.first()).isNull()
  }

  @Test
  fun `test getMovieDetails with success response`() = runTest {
    remote.mockGetMovieDetails(
      response = Result.success(
        JellyseerrMovieDetailsResponse(
          mediaInfo = MovieInfoResponseFactory.pending(),
        ),
      ),
    )

    val result = repository.getMovieDetails(mediaId = 1)

    assertThat(result.first()).isEqualTo(
      JellyseerrMediaInfoFactory.Movie.pending(),
    )
  }

  @Test
  fun `test getMovieDetails with null`() = runTest {
    remote.mockGetMovieDetails(response = Result.success(JellyseerrMovieDetailsResponse(null)))

    val result = repository.getMovieDetails(mediaId = 1)

    assertThat(result.first()).isEqualTo(null)
  }

  @Test
  fun `test getMovieDetails with failure`() = runTest {
    remote.mockGetMovieDetails(response = Result.failure(MissingJellyseerrHostAddressException()))

    val result = repository.getMovieDetails(mediaId = 1)

    assertThat(result.first()).isEqualTo(null)
  }

  @Test
  fun `test getTvDetails with success response`() = runTest {
    remote.mockGetTvDetails(
      response = Result.success(
        JellyseerrTvDetailsResponse(
          mediaInfo = TvInfoResponse(
            status = JellyseerrMediaStatus.AVAILABLE.status,
            seasons = listOf(
              TvSeasonResponse(
                seasonNumber = 1,
                status = JellyseerrMediaStatus.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 2,
                status = JellyseerrMediaStatus.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 3,
                status = JellyseerrMediaStatus.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 4,
                status = JellyseerrMediaStatus.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 5,
                status = JellyseerrMediaStatus.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 6,
                status = JellyseerrMediaStatus.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 7,
                status = JellyseerrMediaStatus.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 8,
                status = JellyseerrMediaStatus.PARTIALLY_AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 9,
                status = JellyseerrMediaStatus.UNKNOWN.status,
              ),
            ),
            requests = listOf(),
            id = 1399,
          ),
        ),
      ),
    )

    val result = repository.getTvDetails(mediaId = 1)

    assertThat(result.first()).isEqualTo(
      JellyseerrMediaInfoFactory.Tv.available().copy(
        seasons = mapOf(
          1 to JellyseerrMediaStatus.AVAILABLE,
          2 to JellyseerrMediaStatus.AVAILABLE,
          3 to JellyseerrMediaStatus.AVAILABLE,
          4 to JellyseerrMediaStatus.AVAILABLE,
          5 to JellyseerrMediaStatus.AVAILABLE,
          6 to JellyseerrMediaStatus.AVAILABLE,
          7 to JellyseerrMediaStatus.AVAILABLE,
          8 to JellyseerrMediaStatus.PARTIALLY_AVAILABLE,
        ),
      ),
    )
  }

  @Test
  fun `test getTvDetails with requests lists`() = runTest {
    remote.mockGetTvDetails(
      response = Result.success(
        JellyseerrTvDetailsResponse(
          mediaInfo = TvInfoResponse(
            status = JellyseerrMediaStatus.AVAILABLE.status,
            seasons = listOf(
              TvSeasonResponse(
                seasonNumber = 1,
                status = JellyseerrMediaStatus.DELETED.status,
              ),
            ),
            requests = listOf(
              MediaInfoRequestResponse(
                seasons = listOf(
                  TvSeasonResponse(
                    seasonNumber = 2,
                    status = JellyseerrMediaStatus.AVAILABLE.status,
                  ),
                  TvSeasonResponse(
                    seasonNumber = 3,
                    status = JellyseerrMediaStatus.AVAILABLE.status,
                  ),
                ),
                status = JellyseerrMediaStatus.PENDING.status,
                createdAt = "2023-10-01T12:00:00Z",
                updatedAt = "2023-10-02T12:00:00Z",
                requestedBy = RequestedByResponse(
                  id = 1,
                  displayName = "User One",
                ),
                id = 1,
              ),
              MediaInfoRequestResponse(
                seasons = listOf(
                  TvSeasonResponse(
                    seasonNumber = 6,
                    status = JellyseerrMediaStatus.PROCESSING.status,
                  ),
                ),
                status = JellyseerrMediaStatus.PROCESSING.status,
                createdAt = "2023-10-03T12:00:00Z",
                updatedAt = "2023-10-04T12:00:00Z",
                requestedBy = RequestedByResponse(
                  id = 2,
                  displayName = "User Two",
                ),
                id = 2,
              ),
            ),
            id = 1399,
          ),
        ),
      ),
    )

    val result = repository.getTvDetails(mediaId = 1)

    assertThat(result.first()).isEqualTo(
      JellyseerrMediaInfo(
        status = JellyseerrMediaStatus.AVAILABLE,
        seasons = mapOf(
          1 to JellyseerrMediaStatus.DELETED,
          2 to JellyseerrMediaStatus.AVAILABLE,
          3 to JellyseerrMediaStatus.AVAILABLE,
          6 to JellyseerrMediaStatus.PROCESSING,
        ),
        mediaId = 1399,
        requests = listOf(
          JellyseerrRequest.TV(
            id = 1,
            status = JellyseerrMediaStatus.PENDING,
            requester = JellyseerrRequester("User One"),
            seasons = listOf(2, 3),
          ),
          JellyseerrRequest.TV(
            id = 2,
            status = JellyseerrMediaStatus.PROCESSING,
            requester = JellyseerrRequester("User Two"),
            seasons = listOf(6),
          ),
        ),
      ),
    )
  }

  @Test
  fun `test getTvDetails with failure`() = runTest {
    remote.mockGetTvDetails(response = Result.success(JellyseerrTvDetailsResponse(null)))

    val result = repository.getTvDetails(mediaId = 1)

    assertThat(result.first()).isEqualTo(null)
  }

  @Test
  fun `test getTvDetails with null`() = runTest {
    remote.mockGetTvDetails(response = Result.failure(MissingJellyseerrHostAddressException()))

    val result = repository.getTvDetails(mediaId = 1)

    assertThat(result.first()).isEqualTo(null)
  }
}
