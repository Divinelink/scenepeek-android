package com.andreolas.movierama.session

interface SessionRepository {

  suspend fun createRequestToken(): Result<RequestToken>
}
