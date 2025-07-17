package com.divinelink.core.model.ui

enum class ViewMode(val value: String) {
  GRID("grid"),
  LIST("list"),
  ;

  companion object {
    fun from(value: String?) = when (value) {
      GRID.value -> GRID
      LIST.value -> LIST
      else -> LIST
    }
  }
}

fun ViewMode.other(): ViewMode = when (this) {
  ViewMode.GRID -> ViewMode.LIST
  ViewMode.LIST -> ViewMode.GRID
}
