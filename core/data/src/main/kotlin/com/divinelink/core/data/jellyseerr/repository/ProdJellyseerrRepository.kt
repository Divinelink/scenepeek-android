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
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import kotlinx.coroutines.flow.Flow
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

  override fun getLocalJellyseerrAccountDetails(): Flow<JellyseerrAccountDetails?> = queries
    .selectAll()
    .asFlow()
    .mapToOneOrNull(context = dispatcher.io)
    .map { entity ->
      entity?.map()
    }

  override suspend fun insertJellyseerrAccountDetails(accountDetails: JellyseerrAccountDetails) {
    queries.insertAccountDetails(accountDetails.mapToEntity())
  }

  override suspend fun clearJellyseerrAccountDetails() {
    queries.removeAccountDetails()
  }

  override suspend fun logout(address: String): Flow<Result<Unit>> = service.logout(address)
    .map { Result.success(Unit) }

  override suspend fun requestMedia(
    body: JellyseerrRequestMediaBodyApi,
  ): Flow<Result<JellyseerrMediaRequest>> = service
    .requestMedia(body)
    .map { Result.success(it.map()) }
}
