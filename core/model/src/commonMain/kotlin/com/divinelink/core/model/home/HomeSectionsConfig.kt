package com.divinelink.core.model.home

data class HomeSectionsConfig(
  val hiddenHomeSections: Set<MediaListSection.Home>,
  val homeSectionsDisplayOrder: List<MediaListSection.Home>,
) {
  fun getVisibleSections(): List<MediaListSection.Home> = homeSectionsDisplayOrder
    .filter { it !in hiddenHomeSections }

  companion object {
    val initial = HomeSectionsConfig(
      hiddenHomeSections = emptySet(),
      homeSectionsDisplayOrder = MediaListSection.homeEntries,
    )
  }
}
