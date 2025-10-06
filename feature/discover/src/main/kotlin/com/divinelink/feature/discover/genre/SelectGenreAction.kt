package com.divinelink.feature.discover.genre

import com.divinelink.core.model.Genre

sealed interface SelectGenreAction {
  data class SelectGenre(val genre: Genre) : SelectGenreAction
}
