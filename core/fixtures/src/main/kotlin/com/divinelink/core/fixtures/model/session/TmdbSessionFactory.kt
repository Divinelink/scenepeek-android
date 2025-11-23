package com.divinelink.core.fixtures.model.session

import com.divinelink.core.model.session.TmdbSession

object TmdbSessionFactory {

  fun full() = TmdbSession(
    sessionId = "sessionId",
    accessToken = AccessTokenFactory.valid(),
  )
}
