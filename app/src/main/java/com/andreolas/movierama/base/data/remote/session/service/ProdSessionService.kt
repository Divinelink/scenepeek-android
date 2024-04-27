package com.andreolas.movierama.base.data.remote.session.service

import com.andreolas.movierama.base.communication.RestClient
import com.andreolas.movierama.base.data.remote.session.dto.CreateRequestTokenResponseApi
import gr.divinelink.core.util.domain.Result
import javax.inject.Inject

class ProdSessionService @Inject constructor(
  private val restClient: RestClient,
) : SessionService {

  override suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi> {
    val url = "${restClient.tmdbUrl}/authentication/token/new"

    return try {
      val response = restClient.get<CreateRequestTokenResponseApi>(url)
      Result.Success(response)
    } finally {
      // todo
    }
  }

  override suspend fun createSession(requestToken: String): Result<CreateRequestTokenResponseApi> {
    TODO("Not yet implemented")
  }
}
