package com.andreolas.movierama.base.data.remote.session.mapper

import com.divinelink.core.model.session.Session
import com.divinelink.core.network.session.model.CreateSessionResponseApi

fun CreateSessionResponseApi.map() = Session(
  id = sessionId
)
