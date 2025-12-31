package com.divinelink.core.model.details.actions

import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequestFactory
import com.divinelink.core.model.details.DetailActionItem
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DetailActionsBuilderTest {

  private val defaultItems = DetailActionItem.defaultItems

  @Test
  fun `test include default items by default`() {
    val actions = buildActions { }
    actions shouldContainExactly defaultItems
  }

  @Test
  fun `test add Request when canRequest=true and withPermission=true`() {
    val actions = buildActions {
      withRequest(canRequest = true, withPermission = true)
    }

    actions shouldContain DetailActionItem.Request
    actions.size shouldBe defaultItems.size + 1
  }

  @Test
  fun `test does not add Request when canRequest=false`() {
    val actions = buildActions {
      withRequest(canRequest = false, withPermission = true)
    }

    actions shouldNotContain DetailActionItem.Request
    actions shouldContainExactly defaultItems
  }

  @Test
  fun `test does not add Request when withPermission=false`() {
    val actions = buildActions {
      withRequest(canRequest = true, withPermission = false)
    }

    actions shouldNotContain DetailActionItem.Request
    actions shouldContainExactly defaultItems
  }

  @Test
  fun `test does not add Request when both are false`() {
    val actions = buildActions {
      withRequest(canRequest = false, withPermission = false)
    }

    actions shouldNotContain DetailActionItem.Request
    actions shouldContainExactly defaultItems
  }

  @Test
  fun `test add ManageMovie when canManage=true and withPermission=true`() {
    val actions = buildActions {
      withManageMovie(canManage = true, withPermission = true)
    }

    actions shouldContain DetailActionItem.ManageMovie
    actions.size shouldBe defaultItems.size + 1
  }

  @Test
  fun `test does not add ManageMovie when canManage=false`() {
    val actions = buildActions {
      withManageMovie(canManage = false, withPermission = true)
    }

    actions shouldNotContain DetailActionItem.ManageMovie
    actions shouldContainExactly defaultItems
  }

  @Test
  fun `test does not add ManageMovie when withPermission=false`() {
    val actions = buildActions {
      withManageMovie(canManage = true, withPermission = false)
    }

    actions shouldNotContain DetailActionItem.ManageMovie
    actions shouldContainExactly defaultItems
  }

  // ============= withManageTv =============

  @Test
  fun `test add ManageTvShow when canManage=true and withPermission=true`() {
    val actions = buildActions {
      withManageTv(canManage = true, withPermission = true, requests = emptyList())
    }

    actions shouldContain DetailActionItem.ManageTvShow
    actions.size shouldBe defaultItems.size + 1
  }

  @Test
  fun `test does not add ManageTvShow when canManage=false`() {
    val actions = buildActions {
      withManageTv(canManage = false, withPermission = true, requests = emptyList())
    }

    actions shouldNotContain DetailActionItem.ManageTvShow
    actions shouldContainExactly defaultItems
  }

  @Test
  fun `test does not add ManageTvShow with manage true but no permission and empty requests`() {
    val actions = buildActions {
      withManageTv(canManage = true, withPermission = false, requests = emptyList())
    }

    actions shouldNotContain DetailActionItem.ManageTvShow
    actions shouldContainExactly defaultItems
  }

  @Test
  fun `test add ManageTvShow with manage true but no permission and empty requests`() {
    val actions = buildActions {
      withManageTv(
        canManage = true,
        withPermission = false,
        requests = listOf(JellyseerrRequestFactory.movie()),
      )
    }

    actions shouldContain DetailActionItem.ManageTvShow
    actions shouldContainExactly defaultItems + DetailActionItem.ManageTvShow
  }

  @Test
  fun `test add multiple items when all conditions are met`() {
    val actions = buildActions {
      withRequest(
        canRequest = true,
        withPermission = true,
      )
      withManageMovie(
        canManage = true,
        withPermission = true,
      )
      withManageTv(
        canManage = true,
        withPermission = true,
        requests = emptyList(),
      )
    }

    actions shouldContain DetailActionItem.Request
    actions shouldContain DetailActionItem.ManageMovie
    actions shouldContain DetailActionItem.ManageTvShow
    actions.size shouldBe defaultItems.size + 3
  }
}
