package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.exception.MissingJellyseerrHostAddressException
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.client.JellyseerrRestClient.Companion.AUTH_ENDPOINT
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrLoginRequestBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.movie.JellyseerrMovieDetailsResponse
import com.divinelink.core.network.jellyseerr.model.radarr.RadarrInstanceResponse
import com.divinelink.core.network.jellyseerr.model.radarr.SonarrInstanceResponse
import com.divinelink.core.network.jellyseerr.model.tv.JellyseerrTvDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdJellyseerrService(private val restClient: JellyseerrRestClient) : JellyseerrService {

  override suspend fun signInWithJellyfin(jellyfinLogin: JellyseerrLoginData): Flow<Unit> = flow {
    val url = "${jellyfinLogin.address}${AUTH_ENDPOINT}/jellyfin"

    val response = restClient.post<JellyseerrLoginRequestBodyApi, Unit>(
      url = url,
      body = JellyseerrLoginRequestBodyApi.Jellyfin(
        username = jellyfinLogin.username.value,
        password = jellyfinLogin.password.value,
        serverType = jellyfinLogin.authMethod.serverType,
      ),
    )

    emit(response)
  }

  override suspend fun signInWithJellyseerr(jellyseerrLogin: JellyseerrLoginData): Flow<Unit> =
    flow {
      val url = "${jellyseerrLogin.address}${AUTH_ENDPOINT}/local"

      val response = restClient.post<JellyseerrLoginRequestBodyApi, Unit>(
        url = url,
        body = JellyseerrLoginRequestBodyApi.Jellyseerr(
          email = jellyseerrLogin.username.value,
          password = jellyseerrLogin.password.value,
        ),
      )

      emit(response)
    }

  override suspend fun fetchAccountDetails(
    address: String,
  ): Flow<JellyseerrAccountDetailsResponseApi> = flow {
    val url = "$address$AUTH_ENDPOINT/me"

    val response = restClient.get<JellyseerrAccountDetailsResponseApi>(url = url)

    emit(response)
  }

  override suspend fun logout(address: String): Result<Unit> = runCatching {
    val url = "$address$AUTH_ENDPOINT/logout"

    restClient.post<Unit, Unit>(url = url, body = Unit)
  }

  override suspend fun requestMedia(
    body: JellyseerrRequestMediaBodyApi,
  ): Flow<JellyseerrRequestMediaResponse> = flow {
    requireNotNull(restClient.hostAddress()) { throw MissingJellyseerrHostAddressException() }

    val url = "${restClient.hostAddress()}/api/v1/request"

    val response = restClient.post<JellyseerrRequestMediaBodyApi, JellyseerrRequestMediaResponse>(
      url = url,
      body = body,
    )

    emit(response)
  }

  override suspend fun getRequestDetails(requestId: Int): Flow<Result<MediaInfoRequestResponse>> =
    flow {
      val hostAddress = restClient.hostAddress()
      if (hostAddress == null) {
        emit(Result.failure(MissingJellyseerrHostAddressException()))
        return@flow
      }

      val result = runCatching {
        val url = "${restClient.hostAddress()}/api/v1/request/$requestId"
        restClient.get<MediaInfoRequestResponse>(url = url)
      }

      emit(result)
    }

  override suspend fun deleteRequest(mediaId: Int): Result<Unit> {
    val hostAddress = restClient.hostAddress()
      ?: return Result.failure(MissingJellyseerrHostAddressException())

    return runCatching {
      val url = "$hostAddress/api/v1/request/$mediaId"
      restClient.delete<Unit>(url = url)
    }
  }

  /**
   * Removes a media item. The `MANAGE_REQUESTS` permission is required to perform this action.
   */
  override suspend fun deleteMedia(mediaId: Int): Result<Unit> {
    val hostAddress = restClient.hostAddress()
      ?: return Result.failure(MissingJellyseerrHostAddressException())

    return runCatching {
      val url = "$hostAddress/api/v1/media/$mediaId"
      restClient.delete<Unit>(url = url)
    }
  }

  /**
   * Removes a media file from radarr/sonarr.
   */
  override suspend fun deleteFile(mediaId: Int): Result<Unit> {
    val hostAddress = restClient.hostAddress()
      ?: return Result.failure(MissingJellyseerrHostAddressException())

    return runCatching {
      val url = "$hostAddress/api/v1/media/$mediaId/file"
      restClient.delete<Unit>(url = url)
    }
  }

  override suspend fun getMovieDetails(
    mediaId: Int,
  ): Flow<Result<JellyseerrMovieDetailsResponse>> = flow {
    val hostAddress = restClient.hostAddress()
    if (hostAddress == null) {
      emit(Result.failure(MissingJellyseerrHostAddressException()))
      return@flow
    }

    val result = runCatching {
      val url = "${restClient.hostAddress()}/api/v1/movie/$mediaId"
      restClient.get<JellyseerrMovieDetailsResponse>(url = url)
    }
    emit(result)
  }

  override suspend fun getTvDetails(mediaId: Int): Flow<Result<JellyseerrTvDetailsResponse>> =
    flow {
      val hostAddress = restClient.hostAddress()
      if (hostAddress == null) {
        emit(Result.failure(MissingJellyseerrHostAddressException()))
        return@flow
      }

      val result = runCatching {
        val url = "${restClient.hostAddress()}/api/v1/tv/$mediaId"
        restClient.get<JellyseerrTvDetailsResponse>(url = url)
      }
      emit(result)
    }

  override suspend fun getRadarrInstances(): Result<List<RadarrInstanceResponse>> = runCatching {
    val hostAddress = restClient.hostAddress() ?: return Result.failure(
      MissingJellyseerrHostAddressException(),
    )

    return runCatching {
      val url = "$hostAddress/api/v1/service/radarr"
      restClient.get<List<RadarrInstanceResponse>>(url = url)
    }
  }

  override suspend fun getSonarrInstances(): Result<List<SonarrInstanceResponse>> = runCatching {
    val hostAddress = restClient.hostAddress() ?: return Result.failure(
      MissingJellyseerrHostAddressException(),
    )

    return runCatching {
      val url = "$hostAddress/api/v1/service/sonarr"
      restClient.get<List<SonarrInstanceResponse>>(url = url)
    }
  }
}
