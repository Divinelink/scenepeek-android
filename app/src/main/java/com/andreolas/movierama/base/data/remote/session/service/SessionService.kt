package com.andreolas.movierama.base.data.remote.session.service

import com.andreolas.movierama.base.data.remote.session.dto.CreateRequestTokenResponseApi

interface SessionService {

  suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi>
}
