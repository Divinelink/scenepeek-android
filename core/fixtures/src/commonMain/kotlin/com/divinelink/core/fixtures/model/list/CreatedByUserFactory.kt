package com.divinelink.core.fixtures.model.list

import com.divinelink.core.model.list.CreatedByUser

object CreatedByUserFactory {

  fun elsa() = CreatedByUser(
    id = "64cc",
    name = "",
    username = "Elsa",
    avatarPath = "/u8tdN.png",
    gravatarHash = "adbd5",
  )

  fun empty() = CreatedByUser(
    id = "",
    name = "",
    username = "",
    avatarPath = null,
    gravatarHash = null,
  )
}
