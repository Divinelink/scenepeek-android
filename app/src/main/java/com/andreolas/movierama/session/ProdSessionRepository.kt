package com.andreolas.movierama.session

import com.andreolas.movierama.base.data.remote.session.mapper.map
import com.andreolas.movierama.base.data.remote.session.service.SessionService
import gr.divinelink.core.util.domain.Result
import javax.inject.Inject

class ProdSessionRepository @Inject constructor(
  private val remote: SessionService,
) : SessionRepository {

  override suspend fun createRequestToken(): Result<RequestToken> {
    val response = remote.createRequestToken()

    if (response is Result.Success) {
      return Result.Success(response.data.map())
    } else {
      return Result.Error(Exception())
    }
  }

  override suspend fun createSession(requestToken: RequestToken): Result<Any> {
    TODO("Not yet implemented")
  }
}
