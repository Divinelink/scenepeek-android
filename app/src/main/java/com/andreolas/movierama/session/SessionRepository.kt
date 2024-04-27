package com.andreolas.movierama.session

import gr.divinelink.core.util.domain.Result

interface SessionRepository {

  suspend fun createRequestToken(): Result<RequestToken>

  suspend fun createSession(requestToken: RequestToken): Result<Any>
}
