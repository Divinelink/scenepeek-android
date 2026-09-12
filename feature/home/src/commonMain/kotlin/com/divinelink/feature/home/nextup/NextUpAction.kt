package com.divinelink.feature.home.nextup

import com.divinelink.core.model.details.Episode

sealed interface NextUpAction {
  data object Retry : NextUpAction
  data class MarkEpisodeAsWatched(val episode: Episode) : NextUpAction
}
