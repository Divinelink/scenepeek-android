package com.divinelink.core.data.jellyseerr.repository

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.jellyseerr.mapper.map
import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.jellyseerr.JellyseerrRequests
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.request.MediaRequestResult
import com.divinelink.core.model.jellyseerr.request.RequestStatusUpdate
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.model.jellyseerr.server.ServerInstanceDetails
import com.divinelink.core.network.Resource
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.mapper.movie.map
import com.divinelink.core.network.jellyseerr.mapper.requests.map
import com.divinelink.core.network.jellyseerr.mapper.server.radarr.map
import com.divinelink.core.network.jellyseerr.mapper.server.sonarr.map
import com.divinelink.core.network.jellyseerr.mapper.tv.map
import com.divinelink.core.network.jellyseerr.model.JellyseerrEditRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import com.divinelink.core.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProdJellyseerrRepository(
  private val service: JellyseerrService,
  private val authRepository: AuthRepository,
  val dispatcher: DispatcherProvider,
) : JellyseerrRepository {

  override suspend fun signInWithJellyfin(loginData: JellyseerrLoginData): Flow<Result<Unit>> =
    service
      .signInWithJellyfin(loginData)
      .map { Result.success(Unit) }

  override suspend fun signInWithJellyseerr(loginData: JellyseerrLoginData): Flow<Result<Unit>> =
    service
      .signInWithJellyseerr(loginData)
      .map { Result.success(Unit) }

  override suspend fun getJellyseerrProfile(
    refresh: Boolean,
    address: String,
  ): Flow<Resource<JellyseerrProfile?>> = networkBoundResource(
    query = {
      authRepository.selectedJellyseerrProfile
    },
    fetch = {
      service.fetchProfile(address).first().map(address)
    },
    saveFetchResult = { remoteData ->
      authRepository.updateJellyseerrProfile(remoteData)
    },
    shouldFetch = { refresh },
  )

  override suspend fun logout(address: String): Result<Unit> = service.logout(address)
    .map { Result.success(Unit) }

  override suspend fun requestMedia(
    body: JellyseerrRequestMediaBodyApi,
  ): Flow<Result<MediaRequestResult>> = service
    .requestMedia(body)
    .map { Result.success(it.map()) }

  override suspend fun deleteRequest(requestId: Int): Result<Unit> = service
    .deleteRequest(requestId)

  override suspend fun deleteMedia(mediaId: Int): Result<Unit> = service
    .deleteMedia(mediaId)

  override suspend fun deleteFile(mediaId: Int): Result<Unit> = service.deleteFile(mediaId)

  override suspend fun getRequestDetails(requestId: Int): Flow<Result<JellyseerrRequest>> = service
    .getRequestDetails(requestId)
    .map {
      val result = it.getOrNull()

      if (result != null) {
        Result.success(result.map())
      } else {
        Result.failure(Exception("Request details not found"))
      }
    }

  override suspend fun getMovieDetails(mediaId: Int): Flow<JellyseerrMediaInfo?> = service
    .getMovieDetails(mediaId)
    .map { it.getOrNull()?.mediaInfo?.map() }

  override suspend fun getTvDetails(mediaId: Int): Flow<JellyseerrMediaInfo?> = service
    .getTvDetails(mediaId)
    .map { it.getOrNull()?.mediaInfo?.map() }

  override suspend fun getRadarrInstances(): Result<List<ServerInstance>> = service
    .getRadarrInstances()
    .map { it.map() }

  override suspend fun getSonarrInstances(): Result<List<ServerInstance>> = service
    .getSonarrInstances()
    .map { it.map() }

  override suspend fun getRadarrInstanceDetails(id: Int): Result<ServerInstanceDetails> = service
    .getRadarrInstanceDetails(id)
    .map { it.map() }

  override suspend fun getSonarrInstanceDetails(id: Int): Result<ServerInstanceDetails> = service
    .getSonarrInstanceDetails(id)
    .map { it.map() }

  override suspend fun getRequests(
    page: Int,
    filter: MediaRequestFilter,
  ): Flow<Result<JellyseerrRequests>> = service
    .getRequests(
      skip = (page - 1) * 5,
      filter = filter,
    )
    .map { Result.success(it.data.map()) }

  override suspend fun updateRequestStatus(
    requestId: Int,
    status: RequestStatusUpdate,
  ): Result<JellyseerrRequest> = service
    .updateRequestStatus(
      requestId = requestId,
      status = status,
    ).map { it.map() }

  override suspend fun retryRequest(requestId: Int): Result<JellyseerrRequest> = service
    .retryRequest(requestId = requestId)
    .map { it.map() }

  override suspend fun editRequest(
    body: JellyseerrEditRequestMediaBodyApi,
  ): Flow<Result<JellyseerrRequest>> = service
    .updateRequest(body)
    .map { Result.success(it.data.map()) }
}
