package com.andreolas.movierama.session.repository

import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.session.RequestToken
import com.divinelink.core.model.session.Session
import com.divinelink.core.network.session.model.CreateSessionRequestApi
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

  suspend fun createRequestToken(): Result<RequestToken>

  suspend fun createSession(token: CreateSessionRequestApi): Result<Session>

  suspend fun deleteSession(sessionId: String): Result<Boolean>

  fun getAccountDetails(sessionId: String): Flow<Result<AccountDetails>>
}
