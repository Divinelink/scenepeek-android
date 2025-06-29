package com.divinelink.feature.user.data

import com.divinelink.core.model.UIText
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

sealed interface UserDataForm<out T : MediaItem.Media> {
  data object Loading : UserDataForm<Nothing>

  sealed interface Error : UserDataForm<Nothing> {
    data object Unauthenticated : Error
    data object Network : Error
    data object Unknown : Error
  }

  data class Data<T : MediaItem.Media>(
    val mediaType: MediaType,
    val data: List<T>,
    val totalResults: Int,
  ) : UserDataForm<T> {
    private val isMovie = mediaType == MediaType.MOVIE

    val emptyResultsUiText: UIText = if (isMovie) { // TODO Handle ratings
      UIText.ResourceText(R.string.feature_user_data_empty_movies_watchlist)
    } else {
      UIText.ResourceText(R.string.feature_user_data_empty_tv_shows_watchlist)
    }

    val isEmpty: Boolean = data.isEmpty()
  }
}
