package com.divinelink.core.model.track

data class UserEpisodes(
  val available: List<NextUpEpisode>,
  val upcoming: List<NextUpEpisode>,
)
