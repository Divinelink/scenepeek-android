package com.divinelink.core.network.session.service

import com.divinelink.core.network.session.model.AccountDetailsResponseApi
import com.divinelink.core.network.session.model.CreateRequestTokenResponseApi
import com.divinelink.core.network.session.model.CreateSessionRequestApi
import com.divinelink.core.network.session.model.CreateSessionResponseApi
import com.divinelink.core.network.session.model.DeleteSessionResponseApi
import kotlinx.coroutines.flow.Flow

interface SessionService {

  suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi>

  suspend fun createSession(requestToken: CreateSessionRequestApi): Result<CreateSessionResponseApi>

  suspend fun deleteSession(sessionId: String): Result<DeleteSessionResponseApi>

  fun getAccountDetails(sessionId: String): Flow<AccountDetailsResponseApi>
}
