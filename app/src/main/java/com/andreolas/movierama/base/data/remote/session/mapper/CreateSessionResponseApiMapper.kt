package com.andreolas.movierama.base.data.remote.session.mapper

import com.andreolas.movierama.session.model.SessionId
import com.divinelink.core.network.session.model.CreateSessionResponseApi

fun CreateSessionResponseApi.map() = SessionId(
  sessionId = sessionId
)
