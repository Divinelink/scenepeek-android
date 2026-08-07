package com.divinelink.core.network.list.util

import com.divinelink.core.model.sort.SortOption
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ListBuildUrlTest {

  @Test
  fun `test buildFetchListDetailsUrl without sortBy option`() {
    buildFetchListDetailsUrl(
      listId = 12345,
      page = 1,
      language = "en",
      sortOption = null,
    ) shouldBe "https://api.themoviedb.org/4/list/12345?language=en&page=1"
  }

  @Test
  fun `test buildFetchListDetailsUrl with sortBy option`() {
    buildFetchListDetailsUrl(
      listId = 12345,
      page = 1,
      language = "en",
      sortOption = SortOption.defaultDiscoverSortOption,
    ) shouldBe "https://api.themoviedb.org/4/list/12345?language=en&page=1&sort_by=popularity.desc"
  }
}
