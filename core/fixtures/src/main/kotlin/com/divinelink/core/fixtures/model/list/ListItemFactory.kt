package com.divinelink.core.fixtures.model.list

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem

object ListItemFactory {

  fun movies() = ListItem(
    id = 8452376,
    name = "Top Movies",
    posterPath = "/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
    description = "A collection of the top favorite movies.",
    public = true,
    numberOfItems = 5,
    updatedAt = "2025-07-19 17:03:41 UTC",
  )

  fun shows() = ListItem(
    id = 8452377,
    name = "Top TV Shows",
    posterPath = "/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
    description = "A collection of the top favorite TV shows.",
    public = false,
    numberOfItems = 10,
    updatedAt = "2025-07-19 16:03:41 UTC",
  )

  fun recommended() = ListItem(
    id = 8452378,
    name = "Recommended for You",
    posterPath = "/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
    description = "A collection of recommended items based on your preferences.",
    public = true,
    numberOfItems = 15,
    updatedAt = "2025-07-19 15:03:41 UTC",
  )

  fun friends() = ListItem(
    id = 8452379,
    name = "Friends' Favorites",
    posterPath = "/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
    description = "A collection of items liked by your friends.",
    public = false,
    numberOfItems = 20,
    updatedAt = "2025-07-19 14:03:41 UTC",
  )

  fun recommendedFriends() = ListItem(
    id = 8452380,
    name = "Recommended by Friends",
    posterPath = "/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
    description = "A collection of items recommended by your friends.",
    public = true,
    numberOfItems = 25,
    updatedAt = "2025-07-19 13:03:41 UTC",
  )

  fun premium() = ListItem(
    id = 8452381,
    name = "Premium Picks",
    posterPath = "/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    backdropPath = "/4JNggqfyJWREqb0enzpUMbvIniV.jpg",
    description = "A collection of premium items curated for you.",
    public = true,
    numberOfItems = 30,
    updatedAt = "2025-07-19 12:03:41 UTC",
  )

  fun empty() = PaginationData<ListItem>(
    list = emptyList(),
    page = 1,
    totalPages = 0,
    totalResults = 0,
  )

  fun page1() = PaginationData(
    list = listOf(
      movies(),
      shows(),
      recommended(),
    ),
    page = 1,
    totalPages = 2,
    totalResults = 6,
  )

  fun page2() = PaginationData(
    list = listOf(
      friends(),
      recommendedFriends(),
      premium(),
    ),
    page = 2,
    totalPages = 2,
    totalResults = 6,
  )
}
