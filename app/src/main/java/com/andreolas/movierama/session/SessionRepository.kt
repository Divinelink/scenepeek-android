package com.andreolas.movierama.session

interface SessionRepository {

  suspend fun createRequestToken(): Result<RequestToken>

  suspend fun createSession(requestToken: RequestToken): Result<Any>
}
