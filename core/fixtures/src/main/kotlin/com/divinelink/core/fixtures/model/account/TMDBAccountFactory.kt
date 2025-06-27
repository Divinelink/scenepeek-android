package com.divinelink.core.fixtures.model.account

import com.divinelink.core.model.account.TMDBAccount

object TMDBAccountFactory {

  fun LoggedIn() = TMDBAccount.LoggedIn(
    AccountDetailsFactory.Pinkman(),
  )
}
