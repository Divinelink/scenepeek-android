package com.divinelink.core.model.details.season

typealias SeasonForms = Map<Int, SeasonForm<*>>

sealed interface SeasonForm<T : SeasonData> {
  data object Loading : SeasonForm<Nothing>
  data object Error : SeasonForm<Nothing>
  data class Content<T : SeasonData>(val data: T) : SeasonForm<T>
}
