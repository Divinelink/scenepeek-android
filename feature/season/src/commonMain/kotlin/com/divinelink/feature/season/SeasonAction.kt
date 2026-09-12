package com.divinelink.feature.season

sealed interface SeasonAction {
  data class OnSelectTab(val index: Int) : SeasonAction
  data class MarkEpisodeAsWatched(
    val episodeId: Long,
    val watched: Boolean,
  ) : SeasonAction
}
