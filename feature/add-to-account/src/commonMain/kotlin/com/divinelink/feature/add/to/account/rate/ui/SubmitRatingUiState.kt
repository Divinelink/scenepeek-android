package com.divinelink.feature.add.to.account.rate.ui

import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class SubmitRatingUiState(
  val value: Float?,
  val title: String,
  val snackbarMessage: SnackbarMessage?,
  val route: Navigation.SubmitRatingRoute,
) {
  companion object {
    fun initial(route: Navigation.SubmitRatingRoute) = SubmitRatingUiState(
      value = route.value,
      title = route.title,
      route = route,
      snackbarMessage = null,
    )
  }
}
