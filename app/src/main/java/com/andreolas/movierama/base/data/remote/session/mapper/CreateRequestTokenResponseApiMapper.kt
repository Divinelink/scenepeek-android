package com.andreolas.movierama.base.data.remote.session.mapper

import com.andreolas.movierama.base.data.remote.session.dto.CreateRequestTokenResponseApi
import com.andreolas.movierama.session.RequestToken

fun CreateRequestTokenResponseApi.map() = RequestToken(
  token = requestToken,
)
