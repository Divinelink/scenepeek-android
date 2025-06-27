package com.divinelink.core.fixtures.model.account

import com.divinelink.core.model.account.TMDBAccount

object TMDBAccountFactory {

  fun loggedIn() = TMDBAccount.LoggedIn(
    AccountDetailsFactory.Pinkman(),
  )

  fun anonymous() = TMDBAccount.Anonymous
}
