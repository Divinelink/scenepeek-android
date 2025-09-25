package com.divinelink.core.network.list.util

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.toStub
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class BuildUrlTest {

  @Test
  fun `test buildAddItemsToListUrl`() {
    buildListItemsUrl(12345) shouldBe "https://api.themoviedb.org/4/list/12345/items"
  }

  @Test
  fun `test buildListUrl`() {
    buildListUrl() shouldBe "https://api.themoviedb.org/4/list"
  }

  @Test
  fun `test buildListWithIdUrl`() {
    buildListWithIdUrl(12345) shouldBe "https://api.themoviedb.org/4/list/12345"
  }

  @Test
  fun `test buildListItemStatusUrl`() {
    buildListItemStatusUrl(
      listId = 12345,
      media = MediaItemFactory.bruceAlmighty().toStub(),
    ) shouldBe "https://api.themoviedb.org/4/list/12345/item_status?media_id=310&media_type=movie"
  }
}
