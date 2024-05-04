package com.andreolas.movierama.base.data.remote.session.mapper

import com.andreolas.movierama.base.data.remote.session.dto.CreateRequestTokenResponseApi
import com.andreolas.movierama.session.model.RequestToken

fun CreateRequestTokenResponseApi.map() = RequestToken(
  token = requestToken,
  expiresAt = expiresAt
)
