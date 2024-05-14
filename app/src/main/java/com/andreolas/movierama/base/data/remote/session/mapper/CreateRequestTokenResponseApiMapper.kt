package com.andreolas.movierama.base.data.remote.session.mapper

import com.divinelink.core.model.session.RequestToken
import com.divinelink.core.network.session.model.CreateRequestTokenResponseApi

fun CreateRequestTokenResponseApi.map() = RequestToken(
  token = requestToken,
  expiresAt = expiresAt
)
