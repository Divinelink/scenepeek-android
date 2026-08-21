package com.divinelink.core.model.jellyseerr.media

import com.divinelink.core.model.ItemState
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class RequestUiItem(
  val request: JellyseerrRequest,
  val canRequestAdvanced: Boolean,
  val canManageRequest: Boolean,
  val mediaState: ItemState<MediaItem.Media>?,
) {
  val actions = buildList {
    when (request.requestStatus) {
      JellyseerrStatus.Request.PENDING -> if (canManageRequest) {
        add(SeerrRequestAction.Approve)
        add(SeerrRequestAction.Decline)
        add(SeerrRequestAction.Edit)
      } else {
        if (canRequestAdvanced) {
          add(SeerrRequestAction.Edit)
        }
        add(SeerrRequestAction.Cancel)
      }
      JellyseerrStatus.Request.APPROVED -> if (canManageRequest) {
        add(SeerrRequestAction.Delete)
        if (request.media.mediaType == MediaType.TV) {
          add(SeerrRequestAction.RemoveFromSonarr)
        } else {
          add(SeerrRequestAction.RemoveFromRadarr)
        }
      }
      JellyseerrStatus.Request.DECLINED -> if (canManageRequest) {
        add(SeerrRequestAction.Delete)
      }
      JellyseerrStatus.Request.FAILED -> if (canManageRequest) {
        add(SeerrRequestAction.Retry)
        add(SeerrRequestAction.Delete)

        if (request.canRemove) {
          if (request.media.mediaType == MediaType.TV) {
            add(SeerrRequestAction.RemoveFromSonarr)
          } else {
            add(SeerrRequestAction.RemoveFromRadarr)
          }
        }
      }
      JellyseerrStatus.Request.UNKNOWN -> Unit
    }
  }

  val homeActions = actions.filterNot {
    it == SeerrRequestAction.Delete ||
      it == SeerrRequestAction.RemoveFromRadarr ||
      it == SeerrRequestAction.RemoveFromSonarr
  }
}
