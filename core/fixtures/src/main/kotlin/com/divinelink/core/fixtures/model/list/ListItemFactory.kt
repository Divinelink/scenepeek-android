package com.divinelink.core.fixtures.model.list

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem

object ListItemFactory {

  fun nonPrivateList() = ListItem(
    id = 8452377,
    name = "Elsolist",
    posterPath = null,
    backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
    description = "This is a new list to test v4 lists",
    public = false,
    numberOfItems = 3,
    updatedAt = "2025-07-19 16:03:41 UTC",
  )

  fun empty() = PaginationData<ListItem>(
    list = emptyList(),
    page = 1,
    totalPages = 0,
    totalResults = 0,
  )

  fun page1() = PaginationData(
    list = listOf(
      nonPrivateList(),
      nonPrivateList().copy(
        id = 8452378,
        name = "Elsolist 2",
        numberOfItems = 5,
        public = true,
        updatedAt = "2025-07-19 15:03:41 UTC",
      ),
      nonPrivateList().copy(
        id = 8452379,
        name = "Elsolist 3",
        numberOfItems = 10,
        public = false,
        updatedAt = "2025-07-19 14:03:41 UTC",
      ),
    ),
    page = 1,
    totalPages = 2,
    totalResults = 6,
  )

  fun page2() = PaginationData(
    list = listOf(
      nonPrivateList().copy(
        id = 8452390,
        name = "List 4",
      ),
      nonPrivateList().copy(
        id = 8452391,
        name = "List 5",
        numberOfItems = 5,
        public = true,
      ),
      nonPrivateList().copy(
        id = 8452392,
        name = "List 6",
        numberOfItems = 10,
        public = false,
      ),
    ),
    page = 2,
    totalPages = 2,
    totalResults = 6,
  )
}
