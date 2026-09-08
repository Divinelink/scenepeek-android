package com.divinelink.core.model.details

sealed interface DetailActionItem {
  data object Rate : DetailActionItem
  data object Watchlist : DetailActionItem
  data object Favorite : DetailActionItem
  data object List : DetailActionItem
  data object Request : DetailActionItem
  data object ManageMovie : DetailActionItem
  data object ManageTvShow : DetailActionItem
  data object AddShow : DetailActionItem

  companion object {
    val defaultItems = listOf(
      Rate,
      Watchlist,
      Favorite,
      List,
    )
    val defaultTvItems = defaultItems.plus(
      AddShow,
    )
  }
}
