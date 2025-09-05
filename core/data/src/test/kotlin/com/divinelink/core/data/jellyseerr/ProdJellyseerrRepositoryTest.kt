package com.divinelink.core.data.jellyseerr

import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.jellyseerr.repository.ProdJellyseerrRepository
import com.divinelink.core.database.Database
import com.divinelink.core.fixtures.core.network.jellyseerr.model.JellyseerrRequestMediaResponseFactory
import com.divinelink.core.fixtures.core.network.jellyseerr.model.MediaInfoRequestResponseFactory
import com.divinelink.core.fixtures.core.network.jellyseerr.model.movie.MovieInfoResponseFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequestFactory
import com.divinelink.core.fixtures.model.jellyseerr.request.JellyseerrMediaRequestResponseFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceFactory
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.MissingJellyseerrHostAddressException
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.network.Resource
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.divinelink.core.network.jellyseerr.model.movie.JellyseerrMovieDetailsResponse
import com.divinelink.core.network.jellyseerr.model.tv.JellyseerrTvDetailsResponse
import com.divinelink.core.network.jellyseerr.model.tv.TvInfoResponse
import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.JellyseerrRequestMediaBodyApiFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.radarr.RadarrInstanceDetailsResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.radarr.RadarrInstanceResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.sonarr.SonarrInstanceDetailsResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.sonarr.SonarrInstanceResponseFactory
import com.divinelink.core.testing.service.TestJellyseerrService
import com.google.common.truth.Truth.assertThat
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdJellyseerrRepositoryTest {

  private lateinit var repository: JellyseerrRepository

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val remote = TestJellyseerrService()

  private lateinit var database: Database

  @BeforeTest
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
        authMethod = JellyseerrAuthMethod.JELLYFIN,
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
        authMethod = JellyseerrAuthMethod.JELLYSEERR,
      ),
    )

    assertThat(result.first()).isEqualTo(Result.success(Unit))
  }

  @Test
  fun `test logout successfully`() = runTest {
    remote.mockLogout(
      response = Result.success(Unit),
    )

    val result = repository.logout(
      address = "http://localhost:8096",
    )

    assertThat(result).isEqualTo(Result.success(Unit))
  }

  @Test
  fun `test tv request media successfully`() = runTest {
    val response = JellyseerrRequestMediaResponseFactory.partiallyAvailableTv()

    val expectedResponse = JellyseerrMediaRequestResponseFactory.tvPartially().copy(
      mediaInfo = JellyseerrMediaInfo(
        mediaId = 134,
        requests = JellyseerrRequestFactory.Tv.all(),
        status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
        seasons = listOf(
          SeasonRequest(1, JellyseerrStatus.Media.PARTIALLY_AVAILABLE),
          SeasonRequest(2, JellyseerrStatus.Media.PARTIALLY_AVAILABLE),
        ),
      ),
    )

    remote.mockRequestMedia(response = response)

    repository.requestMedia(
      body = JellyseerrRequestMediaBodyApiFactory.tv(),
    ).test {
      assertThat(awaitItem()).isInstanceOf(Result.success(expectedResponse)::class.java)
      awaitComplete()
    }
  }

  @Test
  fun `test movie request media successfully`() = runTest {
    val response = JellyseerrRequestMediaResponseFactory.movie(
      status = JellyseerrStatus.Media.PROCESSING,
    )

    val mappedResponse = JellyseerrMediaRequestResponseFactory.movieProcessing()

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
            status = JellyseerrStatus.Media.AVAILABLE.status,
            seasons = listOf(
              TvSeasonResponse(
                seasonNumber = 1,
                status = JellyseerrStatus.Media.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 2,
                status = JellyseerrStatus.Media.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 3,
                status = JellyseerrStatus.Media.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 4,
                status = JellyseerrStatus.Media.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 5,
                status = JellyseerrStatus.Media.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 6,
                status = JellyseerrStatus.Media.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 7,
                status = JellyseerrStatus.Media.AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 8,
                status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE.status,
              ),
              TvSeasonResponse(
                seasonNumber = 9,
                status = JellyseerrStatus.Media.UNKNOWN.status,
              ),
            ),
            requests = listOf(),
            id = 134,
          ),
        ),
      ),
    )

    val result = repository.getTvDetails(mediaId = 1)

    assertThat(result.first()).isEqualTo(
      JellyseerrMediaInfoFactory.Tv.available().copy(
        seasons = listOf(
          SeasonRequest(1, JellyseerrStatus.Media.AVAILABLE),
          SeasonRequest(2, JellyseerrStatus.Media.AVAILABLE),
          SeasonRequest(3, JellyseerrStatus.Media.AVAILABLE),
          SeasonRequest(4, JellyseerrStatus.Media.AVAILABLE),
          SeasonRequest(5, JellyseerrStatus.Media.AVAILABLE),
          SeasonRequest(6, JellyseerrStatus.Media.AVAILABLE),
          SeasonRequest(7, JellyseerrStatus.Media.AVAILABLE),
          SeasonRequest(8, JellyseerrStatus.Media.PARTIALLY_AVAILABLE),
          SeasonRequest(9, JellyseerrStatus.Media.UNKNOWN),
        ),
      ),
    )
  }

  @Test
  fun `test getTvDetails with requests lists also updates seasons from requests`() = runTest {
    remote.mockGetTvDetails(
      response = Result.success(
        JellyseerrTvDetailsResponse(
          mediaInfo = TvInfoResponse(
            status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE.status,
            seasons = listOf(
              TvSeasonResponse(
                seasonNumber = 1,
                status = JellyseerrStatus.Media.DELETED.status,
              ),
            ),
            requests = MediaInfoRequestResponseFactory.all(),
            id = 134,
          ),
        ),
      ),
    )

    repository.getTvDetails(mediaId = 1399).test {
      assertThat(awaitItem()).isInstanceOf(
        JellyseerrMediaInfoFactory.Tv.partiallyAvailable().copy(
          requests = listOf(
            JellyseerrRequestFactory.Tv.betterCallSaul1().copy(
              seasons = listOf(
                SeasonRequest(2, JellyseerrStatus.Season.PENDING),
                SeasonRequest(3, JellyseerrStatus.Season.PENDING),
              ),
            ),
            JellyseerrRequestFactory.Tv.betterCallSaul2(),
            JellyseerrRequestFactory.Tv.betterCallSaul3().copy(
              seasons = listOf(
                SeasonRequest(1, JellyseerrStatus.Season.PROCESSING),
                SeasonRequest(6, JellyseerrStatus.Season.PENDING),
              ),
            ),
          ),
          seasons = listOf(
            SeasonRequest(1, JellyseerrStatus.Media.DELETED),
            SeasonRequest(2, JellyseerrStatus.Media.PROCESSING),
            SeasonRequest(3, JellyseerrStatus.Media.PROCESSING),
            SeasonRequest(5, JellyseerrStatus.Media.UNKNOWN),
          ),
        )::class.java,
      )
      awaitComplete()
    }
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

  @Test
  fun `test getRequestDetails with success returns mapped model`() = runTest {
    remote.mockGetRequestDetails(
      response = Result.success(MediaInfoRequestResponseFactory.betterCallSaul1()),
    )

    val result = repository.getRequestDetails(requestId = 2)

    assertThat(result.first()).isInstanceOf(
      Result.success(
        JellyseerrRequestFactory.Tv.betterCallSaul1(),
      )::class.java,
    )
  }

  @Test
  fun `test getRequestDetails with failure returns exception`() = runTest {
    remote.mockGetRequestDetails(
      response = Result.failure(Exception("Not found")),
    )

    val result = repository.getRequestDetails(requestId = 2)

    assertThat(result.first()).isInstanceOf(
      Result.failure<Exception>(Exception("Request details not found"))::class.java,
    )
  }

  @Test
  fun `test getJellyseerrAccountDetails always fetches from local data`() = runTest {
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

    repository.getJellyseerrAccountDetails(
      refresh = true,
      address = "http://192.168.1.50:5055",
    ).test {
      assertThat(awaitItem()).isEqualTo(Resource.Loading(null))
      assertThat(awaitItem()).isEqualTo(
        Resource.Success(
          JellyseerrAccountDetailsFactory.jellyfin().copy(
            avatar = "http://192.168.1.50:5055/avatarproxy/1dde62cf4a2c436d95e17b9",
          ),
        ),
      )
    }
  }

  @Test
  fun `test getJellyseerrAccountDetails with refresh false does not fetch from remote`() = runTest {
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

    repository.getJellyseerrAccountDetails(
      refresh = false,
      address = "http://192.168.1.50:5055",
    ).test {
      assertThat(awaitItem()).isEqualTo(Resource.Loading(null))
      assertThat(awaitItem()).isEqualTo(Resource.Success(null))
    }
  }

  @Test
  fun `test get radarr instances with success`() = runTest {
    remote.mockGetRadarrInstances(
      Result.success(RadarrInstanceResponseFactory.all),
    )

    repository.getRadarrInstances().data shouldBe RadarrInstanceFactory.all
  }

  @Test
  fun `test get sonarr instances with success`() = runTest {
    remote.mockGetSonarrInstances(
      Result.success(SonarrInstanceResponseFactory.all),
    )

    repository.getSonarrInstances().data shouldBe SonarrInstanceFactory.all
  }

  @Test
  fun `test get radarr instances with failure`() = runTest {
    remote.mockGetRadarrInstances(
      Result.failure(Exception()),
    )

    shouldThrow<Exception> {
      repository.getRadarrInstances().data
    }
  }

  @Test
  fun `test get sonarr instances with failure`() = runTest {
    remote.mockGetSonarrInstances(
      Result.failure(AppException.Unknown()),
    )

    shouldThrow<AppException.Unknown> {
      repository.getSonarrInstances().data
    }
  }

  @Test
  fun `test get radarr instance details with success`() = runTest {
    remote.mockGetRadarrInstanceDetails(
      Result.success(RadarrInstanceDetailsResponseFactory.default),
    )

    repository.getRadarrInstanceDetails(0).data shouldBe RadarrInstanceDetailsFactory.radarr
  }

  @Test
  fun `test get sonarr instance details with success`() = runTest {
    remote.mockGetSonarrInstanceDetails(
      Result.success(SonarrInstanceDetailsResponseFactory.default),
    )

    repository.getSonarrInstanceDetails(0).data shouldBe SonarrInstanceDetailsFactory.sonarr
  }

  @Test
  fun `test get radarr instance details with failure`() = runTest {
    remote.mockGetRadarrInstanceDetails(
      Result.failure(Exception()),
    )

    shouldThrow<Exception> {
      repository.getRadarrInstanceDetails(0).data
    }
  }

  @Test
  fun `test get sonarr instance details with failure`() = runTest {
    remote.mockGetSonarrInstanceDetails(
      Result.failure(AppException.Unknown()),
    )

    shouldThrow<AppException.Unknown> {
      repository.getSonarrInstanceDetails(0).data
    }
  }
}
