package com.divinelink.feature.episode

sealed interface EpisodeAction {
  data class OnSelectEpisode(val index: Int) : EpisodeAction
  data class OnSubmitRate(val rate: Int) : EpisodeAction
  data object OnClearRate : EpisodeAction

  data object DismissSnackbar : EpisodeAction
}
