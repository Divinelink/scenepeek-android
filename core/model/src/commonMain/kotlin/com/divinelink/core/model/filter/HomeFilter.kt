package com.divinelink.core.model.filter

import com.divinelink.core.model.UIText
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.awards
import com.divinelink.core.model.resources.collection
import com.divinelink.core.model.resources.top_rated

sealed class HomeFilter(
  override val value: String,
  override val name: UIText,
  override val isSelected: Boolean,
) : SelectableFilter(
  value = value,
  name = name,
  isSelected = isSelected,
) {

  data class Collection(
    override val isSelected: Boolean,
  ) : HomeFilter(
    value = "collection",
    name = UIText.ResourceText(Res.string.collection),
    isSelected = isSelected,
  )

  data class TopRated(
    override val isSelected: Boolean,
  ) : HomeFilter(
    value = "top_rated",
    name = UIText.ResourceText(Res.string.top_rated),
    isSelected = isSelected,
  )

  data object Awards : HomeFilter(
    value = "awards",
    name = UIText.ResourceText(Res.string.awards),
    isSelected = false,
  )

  companion object {
    val entries
      get() = listOf(
        Collection(isSelected = false),
        Awards,
        TopRated(isSelected = false),
      )
  }
}
