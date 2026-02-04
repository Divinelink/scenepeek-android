package com.divinelink.feature.episode

sealed interface EpisodeAction {
  data class OnSelectEpisode(val index: Int) : EpisodeAction
}
