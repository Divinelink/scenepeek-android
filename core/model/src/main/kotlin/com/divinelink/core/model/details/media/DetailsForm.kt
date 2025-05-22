package com.divinelink.core.model.details.media

typealias DetailsForms = Map<Int, DetailsForm<*>>

sealed interface DetailsForm<T : DetailsData> {
  data object Loading : DetailsForm<Nothing>
  data object Error : DetailsForm<Nothing>
  data class Content<T : DetailsData>(val data: T) : DetailsForm<T>
}
