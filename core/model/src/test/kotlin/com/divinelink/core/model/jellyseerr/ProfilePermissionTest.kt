package com.divinelink.core.model.jellyseerr

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ProfilePermissionTest {

  @Test
  fun `test decode admin permission`() {
    ProfilePermission.decode(2) shouldBe listOf(ProfilePermission.ADMIN)
  }

  @Test
  fun `test decode manage users`() {
    // 8
    ProfilePermission.decode(8) shouldBe listOf(ProfilePermission.MANAGE_USERS)
  }

  @Test
  fun `test decode manage users and requests`() {
    // 8 + 16
    ProfilePermission.decode(24) shouldBe listOf(
      ProfilePermission.MANAGE_USERS,
      ProfilePermission.MANAGE_REQUESTS,
    )
  }

  @Test
  fun `test decode manage users and advanced requests`() {
    // 8 + 8192
    ProfilePermission.decode(8200) shouldBe listOf(
      ProfilePermission.MANAGE_USERS,
      ProfilePermission.REQUEST_ADVANCED,
    )
  }

  @Test
  fun `test decode request and vote`() {
    // 32 + 64
    ProfilePermission.decode(96) shouldBe listOf(
      ProfilePermission.REQUEST,
      ProfilePermission.VOTE,
    )
  }

  @Test
  fun `test decode movie and tv requests`() {
    // 262144 + 524288
    ProfilePermission.decode(786432) shouldBe listOf(
      ProfilePermission.REQUEST_MOVIE,
      ProfilePermission.REQUEST_TV,
    )
  }

  @Test
  fun `test decode 4k movie and tv requests`() {
    // 2048 + 4096
    ProfilePermission.decode(6144) shouldBe listOf(
      ProfilePermission.REQUEST_4K_MOVIE,
      ProfilePermission.REQUEST_4K_TV,
    )
  }

  @Test
  fun `test decode auto approve movie and tv`() {
    // 256 + 512
    ProfilePermission.decode(768) shouldBe listOf(
      ProfilePermission.AUTO_APPROVE_MOVIE,
      ProfilePermission.AUTO_APPROVE_TV,
    )
  }

  @Test
  fun `test decode auto approve 4k movie and tv`() {
    // 65536 + 131072
    ProfilePermission.decode(196608) shouldBe listOf(
      ProfilePermission.AUTO_APPROVE_4K_MOVIE,
      ProfilePermission.AUTO_APPROVE_4K_TV,
    )
  }

  @Test
  fun `test decode issue management permissions`() {
    // 1048576 + 2097152 + 4194304
    ProfilePermission.decode(7340032) shouldBe listOf(
      ProfilePermission.MANAGE_ISSUES,
      ProfilePermission.VIEW_ISSUES,
      ProfilePermission.CREATE_ISSUES,
    )
  }

  @Test
  fun `test decode blacklist permissions`() {
    // 268435456 + 1073741824
    ProfilePermission.decode(1342177280L) shouldBe listOf(
      ProfilePermission.MANAGE_BLACKLIST,
      ProfilePermission.VIEW_BLACKLIST,
    )
  }

  @Test
  fun `test decode recent and watchlist view`() {
    // 67108864 + 134217728
    ProfilePermission.decode(201326592) shouldBe listOf(
      ProfilePermission.RECENT_VIEW,
      ProfilePermission.WATCHLIST_VIEW,
    )
  }

  @Test
  fun `test decode your original 794656 case`() {
    // 32 + 8192 + 262144 + 524288
    ProfilePermission.decode(794656) shouldBe listOf(
      ProfilePermission.REQUEST,
      ProfilePermission.REQUEST_ADVANCED,
      ProfilePermission.REQUEST_MOVIE,
      ProfilePermission.REQUEST_TV,
    )
  }

  @Test
  fun `test decode admin and manage settings`() {
    // 2 + 4
    ProfilePermission.decode(6) shouldBe listOf(
      ProfilePermission.ADMIN,
      ProfilePermission.MANAGE_SETTINGS,
    )
  }

  @Test
  fun `test decode zero returns empty list`() {
    ProfilePermission.decode(0) shouldBe emptyList()
  }

  @Test
  fun `test decode undefined bit returns empty list`() {
    ProfilePermission.decode(1) shouldBe emptyList()
  }

  @Test
  fun `test decode high bit not in enum returns empty list`() {
    // e.g., 2^40
    ProfilePermission.decode(1099511627776L) shouldBe emptyList()
  }

  @Test
  fun `test decode all permissions combined`() {
    val allPermissions = ProfilePermission.entries
      .filter { it != ProfilePermission.NONE }
      .map { it.value }
      .reduce(Long::or)

    val decoded = ProfilePermission.decode(allPermissions)
    val expected = ProfilePermission.entries.filter { it != ProfilePermission.NONE }

    decoded.toSet() shouldBe expected.toSet()
  }
}
