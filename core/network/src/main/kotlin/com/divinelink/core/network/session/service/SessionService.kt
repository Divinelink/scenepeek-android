package com.divinelink.core.network.session.service

import com.divinelink.core.network.session.model.AccountDetailsResponseApi
import com.divinelink.core.network.session.model.CreateRequestTokenResponseApi
import com.divinelink.core.network.session.model.CreateSessionResponseApi
import com.divinelink.core.network.session.model.DeleteSessionResponseApi
import com.divinelink.core.network.session.model.v4.CreateAccessTokenResponse
import kotlinx.coroutines.flow.Flow

interface SessionService {

  suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi>

  suspend fun createAccessToken(requestToken: String): Result<CreateAccessTokenResponse>

  suspend fun createSession(accessToken: String): Result<CreateSessionResponseApi>

  suspend fun logout(accessToken: String): Result<DeleteSessionResponseApi>

  fun getAccountDetails(sessionId: String): Flow<AccountDetailsResponseApi>
}
