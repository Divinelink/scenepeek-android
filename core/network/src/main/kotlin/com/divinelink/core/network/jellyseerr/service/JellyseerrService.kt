package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.movie.JellyseerrMovieDetailsResponse
import com.divinelink.core.network.jellyseerr.model.tv.JellyseerrTvDetailsResponse
import kotlinx.coroutines.flow.Flow

interface JellyseerrService {

  suspend fun signInWithJellyfin(jellyfinLogin: JellyseerrLoginData): Flow<Unit>

  suspend fun signInWithJellyseerr(jellyseerrLogin: JellyseerrLoginData): Flow<Unit>

  suspend fun fetchAccountDetails(address: String): Flow<JellyseerrAccountDetailsResponseApi>

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
}
