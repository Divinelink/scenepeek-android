package com.andreolas.movierama.session.repository

import com.andreolas.movierama.base.data.remote.session.mapper.map
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.session.RequestToken
import com.divinelink.core.model.session.Session
import com.divinelink.core.network.session.model.CreateSessionRequestApi
import com.divinelink.core.network.session.service.SessionService
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdSessionRepository @Inject constructor(
  private val remote: SessionService,
) : SessionRepository {

  override suspend fun createRequestToken(): Result<RequestToken> {
    val response = remote.createRequestToken()

    return Result.success(response.data.map())
  }

  override suspend fun createSession(token: CreateSessionRequestApi): Result<Session> {
    val response = remote.createSession(token)

    return Result.success(response.data.map())
  }

  override suspend fun deleteSession(sessionId: String): Result<Boolean> {
    val response = remote.deleteSession(sessionId = sessionId)

    return Result.success(response.data.success)
  }

  override fun getAccountDetails(sessionId: String): Flow<Result<AccountDetails>> = remote
    .getAccountDetails(sessionId)
    .map { apiResponse ->
      Result.success(apiResponse.map())
    }
    .catch { exception ->
      Result.failure<Exception>(Exception(exception.message))
    }
}
