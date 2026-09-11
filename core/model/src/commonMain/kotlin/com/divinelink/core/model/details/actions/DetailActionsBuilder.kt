package com.divinelink.core.model.details.actions

import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.media.MediaType

class DetailActionsBuilder internal constructor() {
  private val actions = mutableListOf<DetailActionItem>()

  fun applyDefaults(type: MediaType) = apply {
    when (type) {
      MediaType.MOVIE -> actions.addAll(DetailActionItem.defaultItems)
      MediaType.TV -> actions.addAll(DetailActionItem.defaultTvItems)
      else -> Unit
    }
  }

  fun withRequest(
    canRequest: Boolean,
    withPermission: Boolean,
  ): DetailActionsBuilder = apply {
    if (canRequest && withPermission) actions.add(DetailActionItem.Request)
  }

  fun withManageMovie(
    canManage: Boolean,
    withPermission: Boolean,
  ): DetailActionsBuilder = apply {
    if (canManage && withPermission) actions.add(DetailActionItem.ManageMovie)
  }

  fun withManageTv(
    canManage: Boolean,
    withPermission: Boolean,
    requests: List<JellyseerrRequest>,
  ): DetailActionsBuilder = apply {
    if (canManage && (withPermission || requests.isNotEmpty())) {
      actions.add(DetailActionItem.ManageTvShow)
    }
  }

  fun build(): List<DetailActionItem> = actions.toList()
}

fun buildActions(
  type: MediaType,
  configure: DetailActionsBuilder.() -> Unit,
): List<DetailActionItem> = DetailActionsBuilder()
  .applyDefaults(type)
  .apply(configure)
  .build()
