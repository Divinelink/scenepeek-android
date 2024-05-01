package com.andreolas.movierama.base.data.remote.session.service

import com.andreolas.movierama.base.data.remote.session.dto.AccountDetailsResponseApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateRequestTokenResponseApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateSessionRequestApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateSessionResponseApi
import com.andreolas.movierama.base.data.remote.session.dto.delete.DeleteSessionResponseApi
import kotlinx.coroutines.flow.Flow

interface SessionService {

  suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi>

  suspend fun createSession(requestToken: CreateSessionRequestApi): Result<CreateSessionResponseApi>

  suspend fun deleteSession(sessionId: String): Result<DeleteSessionResponseApi>

  fun getAccountDetails(sessionId: String): Flow<AccountDetailsResponseApi>
}
