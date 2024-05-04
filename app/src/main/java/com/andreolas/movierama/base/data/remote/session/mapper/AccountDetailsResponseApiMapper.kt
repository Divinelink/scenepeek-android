package com.andreolas.movierama.base.data.remote.session.mapper

import com.andreolas.movierama.base.data.remote.session.dto.AccountDetailsResponseApi
import com.andreolas.movierama.session.model.AccountDetails

fun AccountDetailsResponseApi.map() = AccountDetails(
  id = id,
  username = username,
  name = name,
)
