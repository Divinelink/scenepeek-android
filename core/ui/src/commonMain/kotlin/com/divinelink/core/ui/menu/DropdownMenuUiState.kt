package com.divinelink.core.ui.menu

import com.divinelink.core.model.ScreenType
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class DropDownMenuUiState(
  val entryPoint: ScreenType,
  val snackbarMessage: SnackbarMessage?,
) {
  companion object {
    fun initial(entryPoint: ScreenType): DropDownMenuUiState = DropDownMenuUiState(
      entryPoint = entryPoint,
      snackbarMessage = null,
    )
  }

  val availableIntents: List<DropdownMenuIntent> = when (entryPoint) {
    is ScreenType.Collection -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.List -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.Movie -> buildList {
      entryPoint.wikidataId?.let { id ->
        add(DropdownMenuIntent.OpenInWikipedia(id))
      }
      add(DropdownMenuIntent.Share)
    }
    is ScreenType.Person -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.Show -> buildList {
      entryPoint.wikidataId?.let { id ->
        add(DropdownMenuIntent.OpenInWikipedia(id))
      }
      add(DropdownMenuIntent.ShowOrHideSpoilers(entryPoint.spoilersObfuscated))
      add(DropdownMenuIntent.Share)
    }
    is ScreenType.Season -> listOf(
      DropdownMenuIntent.Share,
    )
    is ScreenType.Episode -> listOf(
      DropdownMenuIntent.Share,
    )
    ScreenType.Unknown -> emptyList()
  }
}
