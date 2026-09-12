package com.divinelink.feature.season

import com.divinelink.core.model.details.Episode

sealed interface SeasonAction {
  data class OnSelectTab(val index: Int) : SeasonAction
  data class MarkEpisodeAsWatched(val episode: Episode) : SeasonAction
}
