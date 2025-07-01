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
  )

  fun paginationData() = PaginationData(
    list = listOf(
      nonPrivateList(),
      nonPrivateList().copy(
        id = 8452378,
        name = "Elsolist 2",
        numberOfItems = 5,
        public = true,
      ),
      nonPrivateList().copy(
        id = 8452379,
        name = "Elsolist 3",
        numberOfItems = 10,
        public = false,
      ),
    ),
    page = 1,
    totalPages = 1,
    totalResults = 3,
  )
}
