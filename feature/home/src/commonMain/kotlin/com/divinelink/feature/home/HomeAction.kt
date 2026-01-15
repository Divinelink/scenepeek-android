package com.divinelink.feature.home

import com.divinelink.core.model.home.MediaListSection

sealed interface HomeAction {
  data object RetryAll : HomeAction
  data class LoadMore(val section: MediaListSection) : HomeAction
  data class RetrySection(val section: MediaListSection) : HomeAction
}
