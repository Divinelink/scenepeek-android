package com.divinelink.core.data.session.repository

import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.model.session.RequestToken
import com.divinelink.core.model.session.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

  suspend fun createRequestToken(): Result<RequestToken>

  suspend fun createAccessToken(token: String): Result<AccessToken>

  suspend fun createSession(accessToken: String): Result<Session>

  suspend fun deleteSession(accessToken: String): Result<Boolean>

  fun getAccountDetails(sessionId: String): Flow<Result<AccountDetails>>

  suspend fun clearRequestToken()
  suspend fun setRequestToken(token: RequestToken)
  suspend fun retrieveRequestToken(): Result<RequestToken>
}
