package com.divinelink.core.model.filter

import com.divinelink.core.model.UIText
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.favorites
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

  data class Favorites(
    override val isSelected: Boolean,
  ) : HomeFilter(
    value = "favorites",
    name = UIText.ResourceText(Res.string.favorites),
    isSelected = isSelected,
  )

  data class TopRated(
    override val isSelected: Boolean,
  ) : HomeFilter(
    value = "top_rated",
    name = UIText.ResourceText(Res.string.top_rated),
    isSelected = isSelected,
  )

  companion object {
    val entries
      get() = listOf(
        Favorites(isSelected = false),
        TopRated(isSelected = false),
      )
  }
}
