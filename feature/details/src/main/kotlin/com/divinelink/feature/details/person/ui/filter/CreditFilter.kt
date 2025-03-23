package com.divinelink.feature.details.person.ui.filter

import com.divinelink.core.model.UIText
import com.divinelink.feature.details.R

sealed class CreditFilter(val title: UIText) {

  data class Department(
    val department: String,
    val size: Int,
  ) : CreditFilter(title = UIText.StringText("$department ($size)"))

  data object SortReleaseDate : CreditFilter(
    title = UIText.ResourceText(R.string.feature_details_filter_sort_release_date),
  )
}
