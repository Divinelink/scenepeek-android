package com.divinelink.core.data.session.repository

import com.divinelink.core.model.session.RequestToken

object RequestTokenManager {
  private var requestToken: RequestToken? = null

  fun setRequestToken(token: RequestToken?) {
    requestToken = token
  }

  fun retrieveRequestToken(): RequestToken? = requestToken
}
