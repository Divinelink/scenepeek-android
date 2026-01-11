package com.divinelink.feature.home

import com.divinelink.core.model.home.HomeSection

sealed interface HomeAction {
  data object RetryAll : HomeAction
  data class LoadMore(val section: HomeSection) : HomeAction
  data class RetrySection(val section: HomeSection) : HomeAction
}
