package com.divinelink.core.model.home

data class HomeSectionsConfig(
  val hiddenHomeSections: Set<MediaListSection.Home>,
  val homeSectionsDisplayOrder: List<MediaListSection.Home>,
) {
  fun getVisibleSections(
    allAvailableSections: List<MediaListSection.Home>,
  ): List<MediaListSection.Home> = homeSectionsDisplayOrder
    .filter { it !in hiddenHomeSections }
    .intersect(allAvailableSections.toSet())
    .sortedBy { homeSectionsDisplayOrder.indexOf(it) }

  companion object {
    val initial = HomeSectionsConfig(
      hiddenHomeSections = emptySet(),
      homeSectionsDisplayOrder = MediaListSection.homeEntries,
    )
  }
}
