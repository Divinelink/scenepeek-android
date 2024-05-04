package com.andreolas.factories.session.model

import com.andreolas.movierama.session.model.AccountDetails

object AccountDetailsFactory {

  fun Pinkman(): AccountDetails = AccountDetails(
    id = 1,
    username = "Jessee_Pinkman",
    name = "Jessee Pinkman",
  )
}
