package com.andreolas.movierama.base.data.remote.session.service

import com.andreolas.movierama.base.communication.RestClient
import com.andreolas.movierama.base.data.remote.session.dto.AccountDetailsResponseApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateRequestTokenResponseApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateSessionRequestApi
import com.andreolas.movierama.base.data.remote.session.dto.CreateSessionResponseApi
import com.andreolas.movierama.base.data.remote.session.dto.delete.DeleteSessionRequestApi
import com.andreolas.movierama.base.data.remote.session.dto.delete.DeleteSessionResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdSessionService @Inject constructor(
  private val restClient: RestClient,
) : SessionService {

  override suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi> {
    val url = "${restClient.tmdbUrl}/authentication/token/new"

    val response = restClient.get<CreateRequestTokenResponseApi>(url)
    return Result.success(response)
  }

  override suspend fun createSession(
    requestToken: CreateSessionRequestApi
  ): Result<CreateSessionResponseApi> {
    val url = "${restClient.tmdbUrl}/authentication/session/new"

    val response = restClient.post<CreateSessionRequestApi, CreateSessionResponseApi>(
      url = url,
      body = requestToken
    )

    return Result.success(response)
  }

  override suspend fun deleteSession(sessionId: String): Result<DeleteSessionResponseApi> {
    val url = "${restClient.tmdbUrl}/authentication/session"

    val response = restClient.delete<DeleteSessionRequestApi, DeleteSessionResponseApi>(
      url = url,
      body = DeleteSessionRequestApi(sessionId)
    )

    return Result.success(response)
  }

  override fun getAccountDetails(sessionId: String): Flow<AccountDetailsResponseApi> = flow {
    val url = "${restClient.tmdbUrl}/account?session_id=$sessionId"

    val response = restClient.get<AccountDetailsResponseApi>(url = url)

    emit(response)
  }
}
