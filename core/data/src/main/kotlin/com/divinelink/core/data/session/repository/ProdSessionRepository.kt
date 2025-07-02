package com.divinelink.core.data.session.repository

import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.mapper.map
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.model.session.RequestToken
import com.divinelink.core.model.session.Session
import com.divinelink.core.network.session.mapper.map
import com.divinelink.core.network.session.service.SessionService

class ProdSessionRepository(
  private val remote: SessionService,
  private val requestTokenManager: RequestTokenManager,
) : SessionRepository {

  override suspend fun createRequestToken(): Result<RequestToken> {
    val response = remote.createRequestToken()

    return Result.success(response.data.map())
  }

  override suspend fun createAccessToken(token: String): Result<AccessToken> {
    val response = remote.createAccessToken(token)

    return Result.success(response.data.map())
  }

  override suspend fun createSession(accessToken: String): Result<Session> {
    val response = remote.createSession(accessToken)

    return Result.success(response.data.map())
  }

  override suspend fun deleteSession(accessToken: String): Result<Boolean> = remote
    .logout(accessToken)
    .map { it.success }

  override suspend fun getAccountDetails(sessionId: String): Result<AccountDetails> = remote
    .getAccountDetails(sessionId)
    .map { response ->
      response.map()
    }

  override suspend fun setRequestToken(token: RequestToken) {
    requestTokenManager.setRequestToken(token)
  }

  override suspend fun clearRequestToken() {
    requestTokenManager.setRequestToken(null)
  }

  override suspend fun retrieveRequestToken(): Result<RequestToken> {
    val token = requestTokenManager.retrieveRequestToken()
    return if (token == null) {
      Result.failure(SessionException.RequestTokenNotFound())
    } else {
      Result.success(token)
    }
  }
}
