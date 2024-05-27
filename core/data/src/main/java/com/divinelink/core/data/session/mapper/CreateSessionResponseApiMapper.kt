package com.divinelink.core.data.session.mapper

import com.divinelink.core.model.session.Session
import com.divinelink.core.network.session.model.CreateSessionResponseApi

fun CreateSessionResponseApi.map() = Session(
  id = sessionId
)
