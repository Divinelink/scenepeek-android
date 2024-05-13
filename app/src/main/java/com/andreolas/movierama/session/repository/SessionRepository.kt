package com.andreolas.movierama.session.repository

import com.andreolas.movierama.session.model.AccountDetails
import com.andreolas.movierama.session.model.RequestToken
import com.andreolas.movierama.session.model.SessionId
import com.divinelink.core.network.session.model.CreateSessionRequestApi
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

  suspend fun createRequestToken(): Result<RequestToken>

  suspend fun createSession(token: CreateSessionRequestApi): Result<SessionId>

  suspend fun deleteSession(sessionId: String): Result<Boolean>

  fun getAccountDetails(sessionId: String): Flow<Result<AccountDetails>>
}
