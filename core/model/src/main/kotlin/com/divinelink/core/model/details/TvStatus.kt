package com.divinelink.core.model.details

import com.divinelink.core.model.R

enum class TvStatus(
  val value: String,
  val resId: Int,
) {
  ENDED(value = "Ended", resId = R.string.core_model_tv_status_ended),
  RETURNING_SERIES(value = "Returning Series", resId = R.string.core_model_tv_status_continuing),
  CANCELED(value = "Canceled", resId = R.string.core_model_tv_status_canceled),
  UNKNOWN(value = "Unknown", resId = R.string.core_model_tv_status_unknown),
  ;

  companion object {
    fun from(value: String?): TvStatus = when (value) {
      ENDED.value -> ENDED
      RETURNING_SERIES.value -> RETURNING_SERIES
      CANCELED.value -> CANCELED
      UNKNOWN.value -> UNKNOWN
      null -> UNKNOWN
      else -> UNKNOWN
    }
  }
}
