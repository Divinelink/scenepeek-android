package com.andreolas.movierama.session

import com.andreolas.movierama.base.data.remote.session.mapper.map
import com.andreolas.movierama.base.data.remote.session.service.SessionService
import javax.inject.Inject

class ProdSessionRepository @Inject constructor(
  private val remote: SessionService,
) : SessionRepository {

  override suspend fun createRequestToken(): Result<RequestToken> = remote
    .createRequestToken()
    .map { it.map() }
}
