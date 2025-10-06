package com.divinelink.feature.discover

sealed interface FilterModal {
  data object Genre : FilterModal
  data object Language : FilterModal
}
