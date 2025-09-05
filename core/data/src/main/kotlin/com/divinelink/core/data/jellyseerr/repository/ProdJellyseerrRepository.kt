package com.divinelink.core.data.jellyseerr.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.data.jellyseerr.mapper.map
import com.divinelink.core.database.JellyseerrAccountDetailsQueries
import com.divinelink.core.database.jellyseerr.mapper.map
import com.divinelink.core.database.jellyseerr.mapper.mapToEntity
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.request.MediaRequestResult
import com.divinelink.core.model.jellyseerr.server.radarr.RadarrInstance
import com.divinelink.core.model.jellyseerr.server.radarr.RadarrInstanceDetails
import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstance
import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstanceDetails
import com.divinelink.core.network.Resource
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.mapper.movie.map
import com.divinelink.core.network.jellyseerr.mapper.server.radarr.map
import com.divinelink.core.network.jellyseerr.mapper.server.sonarr.map
import com.divinelink.core.network.jellyseerr.mapper.tv.map
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import com.divinelink.core.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProdJellyseerrRepository(
  private val service: JellyseerrService,
  private val queries: JellyseerrAccountDetailsQueries,
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

  override suspend fun getRemoteAccountDetails(
    address: String,
  ): Flow<Result<JellyseerrAccountDetails>> = service
    .fetchAccountDetails(address)
    .map { Result.success(it.map(address)) }

  override suspend fun getJellyseerrAccountDetails(
    refresh: Boolean,
    address: String,
  ): Flow<Resource<JellyseerrAccountDetails?>> = networkBoundResource(
    query = {
      queries
        .selectAll()
        .asFlow()
        .mapToOneOrNull(context = dispatcher.io)
        .map { entity ->
          entity?.map()
        }
    },
    fetch = {
      service.fetchAccountDetails(address).first().map(address)
    },
    saveFetchResult = { remoteData ->
      queries.removeAccountDetails()
      queries.insertAccountDetails(remoteData.mapToEntity())
    },
    shouldFetch = { refresh },
  )

  override suspend fun insertJellyseerrAccountDetails(accountDetails: JellyseerrAccountDetails) {
    queries.removeAccountDetails()
    queries.insertAccountDetails(accountDetails.mapToEntity())
  }

  override suspend fun clearJellyseerrAccountDetails() {
    queries.removeAccountDetails()
  }

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

  override suspend fun getRadarrInstances(): Result<List<RadarrInstance>> = service
    .getRadarrInstances()
    .map { it.map() }

  override suspend fun getSonarrInstances(): Result<List<SonarrInstance>> = service
    .getSonarrInstances()
    .map { it.map() }

  override suspend fun getRadarrInstanceDetails(id: Int): Result<RadarrInstanceDetails> = service
    .getRadarrInstanceDetails(id)
    .map { it.map() }

  override suspend fun getSonarrInstanceDetails(id: Int): Result<SonarrInstanceDetails> = service
    .getSonarrInstanceDetails(id)
    .map { it.map() }
}
