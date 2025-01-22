package com.divinelink.feature.details.person.ui.tab

import com.divinelink.feature.details.R

enum class PersonTab(
  val value: String,
  val titleRes: Int,
) {
  ABOUT("about", R.string.feature_details_person_tab_about),
  MOVIES("movies", R.string.feature_details_person_tab_movies),
  TV_SHOWS("tv_shows", R.string.feature_details_person_tab_tv_shows),
}
