package com.divinelink.core.ui.menu

import com.divinelink.core.model.ScreenType

data class DropDownMenuUiState(
  val entryPoint: ScreenType,
) {
  companion object {
    fun initial(
      entryPoint: ScreenType,
    ): DropDownMenuUiState = DropDownMenuUiState(
      entryPoint = entryPoint,
    )
  }

  val availableIntents = when (entryPoint) {
    is ScreenType.Collection -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.List -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.Movie -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.Person -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.Show -> listOf(
      DropdownMenuIntent.Share,
      DropdownMenuIntent.ShowOrHideSpoilers(entryPoint.spoilersObfuscated),
    )
    is ScreenType.Season -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.Episode -> listOf(
      DropdownMenuIntent.Share,
    )
    ScreenType.Unknown -> emptyList()
  }
}
