package com.divinelink.feature.add.to.account.modal.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.decodeToMediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.MediaItemParameterType
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuModal
import kotlin.reflect.typeOf

fun NavGraphBuilder.defaultMediaActionMenu(onNavigation: (Navigation) -> Unit) {
  dialog<Navigation.ActionMenuRoute.Media>(
    typeMap = mapOf(typeOf<MediaItem>() to MediaItemParameterType),
  ) {
    val route = it.toRoute<Navigation.ActionMenuRoute.Media>()
    val mediaItem = route.encodedMediaItem.decodeToMediaItem()

    ActionMenuModal(
      mediaItem = mediaItem,
      entryPoint = ActionMenuEntryPoint.Other,
      onDismissRequest = { onNavigation(Navigation.Back) },
      onMultiSelect = {},
      onNavigateToAddToList = {
        onNavigation(
          Navigation.AddToListRoute(
            id = mediaItem.id,
            mediaType = mediaItem.mediaType,
          ),
        )
      },
    )
  }
}
