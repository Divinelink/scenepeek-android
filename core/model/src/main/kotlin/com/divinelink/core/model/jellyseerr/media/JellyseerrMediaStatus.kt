package com.divinelink.core.model.jellyseerr.media

enum class JellyseerrMediaStatus(val status: Int) {
  UNKNOWN(1),
  PENDING(2),
  PROCESSING(3),
  PARTIALLY_AVAILABLE(4),
  AVAILABLE(5),
  DELETED(6),
  ;

  companion object {
    private val map = JellyseerrMediaStatus.entries.associateBy { it.status }

    fun from(status: Int): JellyseerrMediaStatus = map[status] ?: UNKNOWN
  }
}
