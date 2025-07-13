package com.divinelink.core.fixtures.model.list

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.list.ListDetails

object ListDetailsFactory {

  fun mustWatch() = ListDetails(
    name = "Must watch",
    media = listOf(
      MediaItemFactory.theWire(),
      MediaItemFactory.FightClub(),
      MediaItemFactory.theOffice(),
    ),
  )
}
