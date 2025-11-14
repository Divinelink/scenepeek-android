package com.divinelink.feature.details.person.ui.filter

import com.divinelink.core.model.UIText
import com.divinelink.feature.details.Res
import com.divinelink.feature.details.feature_details_filter_sort_release_date

sealed class CreditFilter(val title: UIText) {

  data class Department(
    val department: String,
    val size: Int,
  ) : CreditFilter(title = UIText.StringText("$department ($size)"))

  data object SortReleaseDate : CreditFilter(
    title = UIText.ResourceText(Res.string.feature_details_filter_sort_release_date),
  )
}
