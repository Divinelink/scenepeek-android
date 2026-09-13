package com.divinelink.feature.details.media.ui.actions

import com.divinelink.core.model.details.AccountDataSection

sealed interface MediaAction {
  data object AddShow : MediaAction
  data object AddRating : MediaAction
  data object AddToList : MediaAction
  data class AddToAccount(val section: AccountDataSection) : MediaAction

  data object Request : MediaAction
  data object ManageTv : MediaAction
  data object ManageMovie : MediaAction
}
