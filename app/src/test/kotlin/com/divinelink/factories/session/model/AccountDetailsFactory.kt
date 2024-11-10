package com.divinelink.factories.session.model

import com.divinelink.core.model.account.AccountDetails

object AccountDetailsFactory {

  fun Pinkman(): AccountDetails = AccountDetails(
    id = 1,
    username = "Jessee_Pinkman",
    name = "Jessee Pinkman",
  )
}
