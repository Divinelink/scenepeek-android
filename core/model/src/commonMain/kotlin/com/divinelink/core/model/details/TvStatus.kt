package com.divinelink.core.model.details

import com.divinelink.core.model.Res
import com.divinelink.core.model.core_model_tv_status_canceled
import com.divinelink.core.model.core_model_tv_status_continuing
import com.divinelink.core.model.core_model_tv_status_ended
import com.divinelink.core.model.core_model_tv_status_unknown
import org.jetbrains.compose.resources.StringResource

enum class TvStatus(
  val value: String,
  val resId: StringResource,
) {
  ENDED(value = "Ended", resId = Res.string.core_model_tv_status_ended),
  RETURNING_SERIES(value = "Returning Series", resId = Res.string.core_model_tv_status_continuing),
  CANCELED(value = "Canceled", resId = Res.string.core_model_tv_status_canceled),
  UNKNOWN(value = "Unknown", resId = Res.string.core_model_tv_status_unknown),
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
