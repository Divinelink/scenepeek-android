package com.divinelink.core.model.details.actions

import com.divinelink.core.model.details.DetailActionItem

class DetailActionsBuilder {
  private val actions = mutableListOf<DetailActionItem>()

  init {
    actions.addAll(DetailActionItem.defaultItems)
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
  ): DetailActionsBuilder = apply {
    if (canManage && withPermission) actions.add(DetailActionItem.ManageTvShow)
  }

  fun build(): List<DetailActionItem> = actions.toList()
}

fun buildActions(configure: DetailActionsBuilder.() -> Unit): List<DetailActionItem> =
  DetailActionsBuilder().apply(configure).build()
