package com.divinelink.core.network.session.mapper

import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.network.session.model.v4.CreateAccessTokenResponse

fun CreateAccessTokenResponse.map() = AccessToken(
  accessToken = accessToken,
  accountId = accountId,
)
