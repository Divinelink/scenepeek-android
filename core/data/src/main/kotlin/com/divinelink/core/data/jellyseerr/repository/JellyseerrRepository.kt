package com.divinelink.core.data.jellyseerr.repository

import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import kotlinx.coroutines.flow.Flow

interface JellyseerrRepository {

  suspend fun signInWithJellyfin(loginData: JellyseerrLoginData): Flow<Result<Unit>>

  suspend fun signInWithJellyseerr(loginData: JellyseerrLoginData): Flow<Result<Unit>>

  suspend fun getRemoteAccountDetails(address: String): Flow<Result<JellyseerrAccountDetails>>

  fun getLocalJellyseerrAccountDetails(): Flow<JellyseerrAccountDetails?>

  suspend fun insertJellyseerrAccountDetails(accountDetails: JellyseerrAccountDetails)

  suspend fun clearJellyseerrAccountDetails()

  suspend fun logout(address: String): Flow<Result<Unit>>

  suspend fun requestMedia(
    body: JellyseerrRequestMediaBodyApi,
  ): Flow<Result<JellyseerrMediaRequest>>
}
