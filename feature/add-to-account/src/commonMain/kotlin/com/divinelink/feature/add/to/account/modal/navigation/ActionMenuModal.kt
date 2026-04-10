package com.divinelink.feature.add.to.account.modal.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.divinelink.core.model.media.decodeToMediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuModal

fun EntryProviderScope<Navigation>.defaultMediaActionMenu(onNavigation: (Navigation) -> Unit) {
  entry<Navigation.ActionMenuRoute.Media>(metadata = DialogSceneStrategy.dialog()) { key ->
    val mediaItem = key.encodedMediaItem.decodeToMediaItem()

    ActionMenuModal(
      mediaItem = mediaItem,
      entryPoint = ActionMenuEntryPoint.Other,
      onDismissRequest = { onNavigation(Navigation.Back) },
      onMultiSelect = {},
      onNavigateToAddToList = {
        onNavigation(
          Navigation.AddToListRoute(
            id = mediaItem.id,
            mediaType = mediaItem.mediaType.value,
          ),
        )
      },
    )
  }
}
