package com.divinelink.feature.user.data

import androidx.annotation.StringRes
import com.divinelink.core.model.media.MediaType

enum class MediaTab(
  @StringRes val titleRes: Int,
  val value: String,
) {
  MOVIE(R.string.feature_user_data_movie_tab, MediaType.MOVIE.value),
  TV(R.string.feature_user_data_tv_show_tab, MediaType.TV.value),
}
