package com.divinelink.feature.discover

sealed interface FilterModal {
  data object Genre : FilterModal
  data object Year : FilterModal
  data object Language : FilterModal
  data object Country : FilterModal
  data object VoteAverage : FilterModal
  data object Keywords : FilterModal
  data object Companies : FilterModal
}
