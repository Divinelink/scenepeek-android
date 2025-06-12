package com.divinelink.core.model.jellyseerr.media

import com.divinelink.core.model.R

enum class JellyseerrMediaStatus(
  val status: Int,
  val resourceId: Int,
) {
  UNKNOWN(1, R.string.core_model_status_unknown),
  PENDING(2, R.string.core_model_status_pending),
  PROCESSING(3, R.string.core_model_status_processing),
  PARTIALLY_AVAILABLE(4, R.string.core_model_status_partially_available),
  AVAILABLE(5, R.string.core_model_status_available),
  DELETED(6, R.string.core_model_status_deleted),
  ;

  companion object {
    private val map = JellyseerrMediaStatus.entries.associateBy { it.status }

    fun from(status: Int): JellyseerrMediaStatus = map[status] ?: UNKNOWN
  }
}
