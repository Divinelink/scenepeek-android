package com.divinelink.core.model.details.actions

import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest

class DetailActionsBuilder internal constructor(
  currentActions: List<DetailActionItem>,
) {
  private val actions = mutableListOf<DetailActionItem>()

  init {
    actions.addAll(currentActions)
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
  currentActions: List<DetailActionItem>,
  configure: DetailActionsBuilder.() -> Unit,
): List<DetailActionItem> = DetailActionsBuilder(currentActions)
  .apply(configure)
  .build()
