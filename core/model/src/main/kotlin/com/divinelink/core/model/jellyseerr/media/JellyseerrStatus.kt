package com.divinelink.core.model.jellyseerr.media

import com.divinelink.core.model.R

sealed interface JellyseerrStatus {
  val status: Int
  val resourceId: Int
  val value: String?

  enum class Request(
    override val status: Int,
    override val resourceId: Int,
    override val value: String?,
  ) : JellyseerrStatus {
    UNKNOWN(0, R.string.core_model_status_unknown, null),
    PENDING(1, R.string.core_model_request_status_pending, "request-pending"),
    APPROVED(2, R.string.core_model_request_status_approved, "request-approved"),
    DECLINED(3, R.string.core_model_request_status_declined, "request-declined"),
    FAILED(4, R.string.core_model_request_status_failed, "request-failed"),
    ;

    companion object {
      private val map = Request.entries.associateBy { it.status }
      private val valueMap = Request.entries.associateBy { it.value }
      fun from(status: Int?): Request = map[status] ?: UNKNOWN
      fun from(value: String?): Request? = valueMap[value]
    }
  }

  enum class Season(
    override val status: Int,
    override val resourceId: Int,
    override val value: String?,
  ) : JellyseerrStatus {
    UNKNOWN(0, R.string.core_model_status_unknown, null),
    PENDING(1, R.string.core_model_request_status_pending, "season-pending"),
    PROCESSING(2, R.string.core_model_status_requested, "season-processing"),
    ;

    companion object {
      private val map = Season.entries.associateBy { it.status }
      private val valueMap = Season.entries.associateBy { it.value }
      fun from(status: Int?): Season = map[status] ?: UNKNOWN
      fun from(value: String?): Season? = valueMap[value]
    }
  }

  enum class Media(
    override val status: Int,
    override val resourceId: Int,
    override val value: String?,
  ) : JellyseerrStatus {
    UNKNOWN(1, R.string.core_model_status_unknown, null),
    PENDING(2, R.string.core_model_status_pending, "media-requested"),
    PROCESSING(3, R.string.core_model_status_requested, "media-pending"),
    PARTIALLY_AVAILABLE(
      4,
      R.string.core_model_status_partially_available,
      "media-partially-available",
    ),
    AVAILABLE(5, R.string.core_model_status_available, "media-available"),
    DELETED(6, R.string.core_model_status_deleted, "media-deleted"),
    ;

    companion object {
      private val statusMap = Media.entries.associateBy { it.status }
      private val valueMap = Media.entries.associateBy { it.value }
      fun from(status: Int?): Media = statusMap[status] ?: UNKNOWN
      fun from(value: String?): Media? = valueMap[value]
    }
  }

  companion object {
    fun from(value: String?): JellyseerrStatus? = when {
      Request.from(value) != null -> Request.from(value)
      Media.from(value) != null -> Media.from(value)
      Season.from(value) != null -> Season.from(value)
      else -> null
    }
  }
}
