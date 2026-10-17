package com.divinelink.core.domain.components.submit

sealed interface SubmitRatingAction {

  data object DismissSnackbar : SubmitRatingAction
  data object ClearRating : SubmitRatingAction
  data class SubmitRating(val rating: Float) : SubmitRatingAction
}
