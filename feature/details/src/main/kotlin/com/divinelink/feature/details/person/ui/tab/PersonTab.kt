package com.divinelink.feature.details.person.ui.tab

import com.divinelink.feature.details.R

enum class PersonTab(
  val order: Int,
  val value: String,
  val titleRes: Int,
) {
  ABOUT(
    order = 0,
    value = "about",
    titleRes = R.string.feature_details_person_tab_about,
  ),
  MOVIES(
    order = 1,
    value = "movies",
    titleRes = R.string.feature_details_person_tab_movies,
  ),
  TV_SHOWS(
    order = 2,
    value = "tv_shows",
    titleRes = R.string.feature_details_person_tab_tv_shows,
  ),
}
