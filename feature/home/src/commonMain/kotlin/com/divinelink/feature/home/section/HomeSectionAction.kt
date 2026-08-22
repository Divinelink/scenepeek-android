package com.divinelink.feature.home.section

import com.divinelink.core.model.home.MediaListSection

sealed interface HomeSectionAction {
  data class LoadMore(val section: MediaListSection) : HomeSectionAction
  data class RetrySection(val section: MediaListSection) : HomeSectionAction
}
