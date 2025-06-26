package com.divinelink.core.model.jellyseerr.media

import com.divinelink.core.model.R

sealed interface JellyseerrStatus {
  val status: Int
  val resourceId: Int

  enum class Request(
    override val status: Int,
    override val resourceId: Int,
  ) : JellyseerrStatus {
    UNKNOWN(0, R.string.core_model_status_unknown),
    PENDING(1, R.string.core_model_request_status_pending),
    APPROVED(2, R.string.core_model_request_status_approved),
    DECLINED(3, R.string.core_model_request_status_declined),
    FAILED(4, R.string.core_model_request_status_failed),
    ;

    companion object {
      private val map = Request.entries.associateBy { it.status }
      fun from(status: Int?): Request = map[status] ?: UNKNOWN
    }
  }

  enum class Season(
    override val status: Int,
    override val resourceId: Int,
  ) : JellyseerrStatus {
    UNKNOWN(0, R.string.core_model_status_unknown),
    PENDING(1, R.string.core_model_request_status_pending),
    PROCESSING(2, R.string.core_model_status_requested),
    ;

    companion object {
      private val map = Season.entries.associateBy { it.status }
      fun from(status: Int?): Season = map[status] ?: UNKNOWN
    }
  }

  enum class Media(
    override val status: Int,
    override val resourceId: Int,
  ) : JellyseerrStatus {
    UNKNOWN(1, R.string.core_model_status_unknown),
    PENDING(2, R.string.core_model_status_pending),
    PROCESSING(3, R.string.core_model_status_requested),
    PARTIALLY_AVAILABLE(4, R.string.core_model_status_partially_available),
    AVAILABLE(5, R.string.core_model_status_available),
    DELETED(6, R.string.core_model_status_deleted),
    ;

    companion object {
      private val map = Media.entries.associateBy { it.status }
      fun from(status: Int?): Media = map[status] ?: UNKNOWN
    }
  }
}
