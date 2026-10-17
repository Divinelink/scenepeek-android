package com.divinelink.feature.episode

sealed interface EpisodeAction {
  data class OnSelectEpisode(val index: Int) : EpisodeAction
  data object MarkAsWatched : EpisodeAction
  data class SetRatingState(val loading: Boolean) : EpisodeAction

  data object DismissSnackbar : EpisodeAction
}
