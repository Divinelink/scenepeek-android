package com.divinelink.core.data.session.mapper

import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.network.session.model.AccountDetailsResponseApi

fun AccountDetailsResponseApi.map() = AccountDetails(
  id = id,
  username = username,
  name = name,
)
