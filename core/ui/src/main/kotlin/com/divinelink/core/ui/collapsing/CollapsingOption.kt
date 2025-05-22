package com.divinelink.core.ui.collapsing

sealed class CollapsingOption(
  val collapsingWhenTop: Boolean,
  val isAutoSnap: Boolean,
) {
  data object EnterAlways : CollapsingOption(collapsingWhenTop = false, isAutoSnap = false)
  data object EnterAlwaysCollapsed : CollapsingOption(collapsingWhenTop = true, isAutoSnap = false)
  data object EnterAlwaysAutoSnap : CollapsingOption(collapsingWhenTop = false, isAutoSnap = true)
  data object EnterAlwaysCollapsedAutoSnap :
    CollapsingOption(collapsingWhenTop = true, isAutoSnap = true)

  companion object {
    private val optionList by lazy {
      listOf(
        EnterAlways,
        EnterAlwaysCollapsed,
        EnterAlwaysAutoSnap,
        EnterAlwaysCollapsedAutoSnap,
      )
    }

    fun toIndex(option: CollapsingOption): Int = optionList.indexOf(option)
    fun toOption(id: Int): CollapsingOption = optionList[id]
  }
}
