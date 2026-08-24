package com.divinelink.core.model.home

/**
 * @param hidden a set of hidden home sections
 * @param order the display order of home sections
 */
data class HomeSectionsConfig(
  val order: List<MediaListSection.Home>,
  val hidden: Set<MediaListSection.Home>,
) {
  fun visibleSections(): List<MediaListSection.Home> = order
    .filter { it !in hidden }

  companion object {
    val initial = HomeSectionsConfig(
      hidden = emptySet(),
      order = MediaListSection.homeEntries,
    )
  }
}
