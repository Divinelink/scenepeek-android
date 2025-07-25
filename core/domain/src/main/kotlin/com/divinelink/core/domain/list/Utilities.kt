package com.divinelink.core.domain.list

import com.divinelink.core.model.list.ListItem

fun mergeListItems(
  page: Int,
  existingItems: List<ListItem>,
  newItems: List<ListItem>,
): List<ListItem> {
  val startIndex = page * 20
  val endIndex = startIndex + 20

  // Keep items outside the page range
  val itemsBeforeRange = existingItems.take(startIndex)
  val itemsAfterRange = existingItems.drop(endIndex)

  // Replace the page range with new items
  return itemsBeforeRange + newItems + itemsAfterRange
}

val listsComparator: Comparator<ListItem> = compareByDescending<ListItem> { item ->
  item.updatedAt
}.thenByDescending { item ->
  item.id
}
