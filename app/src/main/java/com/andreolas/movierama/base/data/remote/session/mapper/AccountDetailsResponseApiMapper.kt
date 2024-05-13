package com.andreolas.movierama.base.data.remote.session.mapper

import com.andreolas.movierama.session.model.AccountDetails
import com.divinelink.core.network.session.model.AccountDetailsResponseApi

fun AccountDetailsResponseApi.map() = AccountDetails(
  id = id,
  username = username,
  name = name,
)
