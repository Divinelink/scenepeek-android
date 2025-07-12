package com.divinelink.core.network.list.util

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class BuildUrlTest {

  @Test
  fun `test buildAddItemsToListUrl`() {
    assertThat(
      buildAddItemsToListUrl(12345),
    ).isEqualTo(
      "https://api.themoviedb.org/4/list/12345/items",
    )
  }
}
