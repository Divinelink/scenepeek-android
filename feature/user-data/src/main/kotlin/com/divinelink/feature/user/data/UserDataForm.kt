package com.divinelink.feature.user.data

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
    val isEmpty: Boolean = data.isEmpty()
  }
}
