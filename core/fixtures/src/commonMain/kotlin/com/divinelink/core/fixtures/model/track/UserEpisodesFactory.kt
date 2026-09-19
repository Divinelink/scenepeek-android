package com.divinelink.core.fixtures.model.track

import com.divinelink.core.model.track.UserEpisodes

object UserEpisodesFactory {

  val empty = UserEpisodes(
    available = emptyList(),
    upcoming = emptyList(),
    paused = emptyList(),
  )
}
