package com.divinelink.core.fixtures.feature.home

import com.divinelink.core.model.home.HomeForm
import com.divinelink.core.model.media.MediaItem

object HomeFormFactory {

  val initial = HomeForm.Initial

  val error = HomeForm.Error

  val empty = HomeForm.Data(
    pages = mapOf(1 to emptyList()),
    canLoadMore = false,
    isLoading = false,
    hasError = false,
  )

  fun data(pages: Map<Int, List<MediaItem>>) = HomeForm.Data(
    pages = pages,
    canLoadMore = false,
    isLoading = false,
    hasError = false,
  )
}
