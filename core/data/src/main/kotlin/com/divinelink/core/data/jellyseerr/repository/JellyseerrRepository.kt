package com.divinelink.core.data.jellyseerr.repository

import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.request.MediaRequestResult
import com.divinelink.core.network.Resource
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import kotlinx.coroutines.flow.Flow

interface JellyseerrRepository {

  suspend fun signInWithJellyfin(loginData: JellyseerrLoginData): Flow<Result<Unit>>

  suspend fun signInWithJellyseerr(loginData: JellyseerrLoginData): Flow<Result<Unit>>

  suspend fun getRemoteAccountDetails(address: String): Flow<Result<JellyseerrAccountDetails>>

  suspend fun getJellyseerrAccountDetails(
    refresh: Boolean,
    address: String,
  ): Flow<Resource<JellyseerrAccountDetails?>>

  suspend fun insertJellyseerrAccountDetails(accountDetails: JellyseerrAccountDetails)

  suspend fun clearJellyseerrAccountDetails()

  suspend fun logout(address: String): Result<Unit>

  suspend fun deleteRequest(requestId: Int): Result<Unit>

  suspend fun deleteMedia(mediaId: Int): Result<Unit>

  suspend fun deleteFile(mediaId: Int): Result<Unit>

  suspend fun requestMedia(body: JellyseerrRequestMediaBodyApi): Flow<Result<MediaRequestResult>>

  suspend fun getRequestDetails(requestId: Int): Flow<Result<JellyseerrRequest>>

  suspend fun getMovieDetails(mediaId: Int): Flow<JellyseerrMediaInfo?>

  suspend fun getTvDetails(mediaId: Int): Flow<JellyseerrMediaInfo?>
}
