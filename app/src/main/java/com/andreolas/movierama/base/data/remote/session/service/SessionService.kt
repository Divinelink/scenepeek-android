package com.andreolas.movierama.base.data.remote.session.service

import com.andreolas.movierama.base.data.remote.session.dto.CreateRequestTokenResponseApi
import gr.divinelink.core.util.domain.Result

interface SessionService {

  suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi>

  suspend fun createSession(requestToken: String): Result<CreateRequestTokenResponseApi>
}
