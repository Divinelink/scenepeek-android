package com.andreolas.movierama.base.data.remote.session.service

import com.andreolas.movierama.base.data.remote.session.dto.AccountDetailsResponseApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateRequestTokenResponseApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateSessionRequestApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateSessionResponseApi
import kotlinx.coroutines.flow.Flow

interface SessionService {

  suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi>

  suspend fun createSession(requestToken: CreateSessionRequestApi): Result<CreateSessionResponseApi>

  fun getAccountDetails(sessionId: String): Flow<AccountDetailsResponseApi>
}
