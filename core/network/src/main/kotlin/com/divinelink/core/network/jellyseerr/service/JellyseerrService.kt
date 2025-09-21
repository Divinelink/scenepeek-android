package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.request.RequestStatusUpdate
import com.divinelink.core.network.jellyseerr.model.JellyseerrProfileResponse
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.movie.JellyseerrMovieDetailsResponse
import com.divinelink.core.network.jellyseerr.model.requests.MediaRequestsResponse
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceDetailsResponse
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceResponse
import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceDetailsResponse
import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceResponse
import com.divinelink.core.network.jellyseerr.model.tv.JellyseerrTvDetailsResponse
import kotlinx.coroutines.flow.Flow

interface JellyseerrService {

  suspend fun signInWithJellyfin(jellyfinLogin: JellyseerrLoginData): Flow<Unit>

  suspend fun signInWithJellyseerr(jellyseerrLogin: JellyseerrLoginData): Flow<Unit>

  suspend fun fetchProfile(address: String): Flow<JellyseerrProfileResponse>

  suspend fun logout(address: String): Result<Unit>

  suspend fun requestMedia(
    body: JellyseerrRequestMediaBodyApi,
  ): Flow<JellyseerrRequestMediaResponse>

  suspend fun deleteRequest(mediaId: Int): Result<Unit>

  suspend fun deleteMedia(mediaId: Int): Result<Unit>

  suspend fun deleteFile(mediaId: Int): Result<Unit>

  suspend fun getRequestDetails(requestId: Int): Flow<Result<MediaInfoRequestResponse>>

  suspend fun getMovieDetails(mediaId: Int): Flow<Result<JellyseerrMovieDetailsResponse>>

  suspend fun getTvDetails(mediaId: Int): Flow<Result<JellyseerrTvDetailsResponse>>

  suspend fun getRadarrInstances(): Result<List<RadarrInstanceResponse>>

  suspend fun getSonarrInstances(): Result<List<SonarrInstanceResponse>>

  suspend fun updateRequestStatus(
    requestId: Int,
    status: RequestStatusUpdate,
  ): Result<MediaInfoRequestResponse>

  suspend fun retryRequest(requestId: Int): Result<MediaInfoRequestResponse>

  suspend fun getRadarrInstanceDetails(id: Int): Result<RadarrInstanceDetailsResponse>

  suspend fun getSonarrInstanceDetails(id: Int): Result<SonarrInstanceDetailsResponse>

  suspend fun getRequests(
    skip: Int,
    filter: MediaRequestFilter,
  ): Flow<Result<MediaRequestsResponse>>
}
