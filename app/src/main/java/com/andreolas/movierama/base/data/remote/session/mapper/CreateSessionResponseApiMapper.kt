package com.andreolas.movierama.base.data.remote.session.mapper

import com.andreolas.movierama.base.data.remote.session.dto.CreateSessionResponseApi
import com.andreolas.movierama.session.model.SessionId

fun CreateSessionResponseApi.map() = SessionId(
  sessionId = sessionId
)
