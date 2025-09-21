package com.divinelink.core.model.jellyseerr.permission

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ProfilePermissionExtensionsTest {

  @Test
  fun `test canPerform anything as admin is always true`() {
    ProfilePermission.entries.forEach { permission ->
      listOf(ProfilePermission.ADMIN).canPerform(permission) shouldBe true
    }
  }

  @Test
  fun `test can manage request with permission is true`() {
    listOf(
      ProfilePermission.MANAGE_REQUESTS,
    ).canPerform(ProfilePermission.MANAGE_REQUESTS) shouldBe true
  }

  @Test
  fun `test can manage request without permission is false`() {
    emptyList<ProfilePermission>().canPerform(ProfilePermission.MANAGE_REQUESTS) shouldBe false
  }

  @Test
  fun `test canManageRequests as admin is true`() {
    listOf(ProfilePermission.ADMIN).canManageRequests() shouldBe true
  }

  @Test
  fun `test canManageRequests with MANAGE_REQUESTS permission is true`() {
    listOf(ProfilePermission.MANAGE_REQUESTS).canManageRequests() shouldBe true
  }

  @Test
  fun `test canManageRequests without permission is false`() {
    emptyList<ProfilePermission>().canManageRequests() shouldBe false
    listOf(ProfilePermission.REQUEST).canManageRequests() shouldBe false
  }

  @Test
  fun `test canRequest TV as admin is true`() {
    listOf(ProfilePermission.ADMIN).canRequest(isTV = true) shouldBe true
  }

  @Test
  fun `test canRequest TV with general REQUEST permission is true`() {
    listOf(ProfilePermission.REQUEST).canRequest(isTV = true) shouldBe true
  }

  @Test
  fun `test canRequest TV with REQUEST_TV permission is true`() {
    listOf(ProfilePermission.REQUEST_TV).canRequest(isTV = true) shouldBe true
  }

  @Test
  fun `test canRequest TV with REQUEST_MOVIE permission is false`() {
    listOf(ProfilePermission.REQUEST_MOVIE).canRequest(isTV = true) shouldBe false
  }

  @Test
  fun `test canRequest TV without permission is false`() {
    emptyList<ProfilePermission>().canRequest(isTV = true) shouldBe false
  }

  @Test
  fun `test canRequest movie as admin is true`() {
    listOf(ProfilePermission.ADMIN).canRequest(isTV = false) shouldBe true
  }

  @Test
  fun `test canRequest movie with general REQUEST permission is true`() {
    listOf(ProfilePermission.REQUEST).canRequest(isTV = false) shouldBe true
  }

  @Test
  fun `test canRequest movie with REQUEST_MOVIE permission is true`() {
    listOf(ProfilePermission.REQUEST_MOVIE).canRequest(isTV = false) shouldBe true
  }

  @Test
  fun `test canRequest movie with REQUEST_TV permission is false`() {
    listOf(ProfilePermission.REQUEST_TV).canRequest(isTV = false) shouldBe false
  }

  @Test
  fun `test canRequest movie without permission is false`() {
    emptyList<ProfilePermission>().canRequest(isTV = false) shouldBe false
  }

  @Test
  fun `test canRequest4K TV as admin is true`() {
    listOf(ProfilePermission.ADMIN).canRequest4K(isTV = true) shouldBe true
  }

  @Test
  fun `test canRequest4K TV with general REQUEST_4K permission is true`() {
    listOf(ProfilePermission.REQUEST_4K).canRequest4K(isTV = true) shouldBe true
  }

  @Test
  fun `test canRequest4K TV with REQUEST_4K_TV permission is true`() {
    listOf(ProfilePermission.REQUEST_4K_TV).canRequest4K(isTV = true) shouldBe true
  }

  @Test
  fun `test canRequest4K TV with REQUEST_4K_MOVIE permission is false`() {
    listOf(ProfilePermission.REQUEST_4K_MOVIE).canRequest4K(isTV = true) shouldBe false
  }

  @Test
  fun `test canRequest4K TV without permission is false`() {
    emptyList<ProfilePermission>().canRequest4K(isTV = true) shouldBe false
    listOf(ProfilePermission.REQUEST_TV).canRequest4K(isTV = true) shouldBe false
  }

  @Test
  fun `test canRequest4K movie as admin is true`() {
    listOf(ProfilePermission.ADMIN).canRequest4K(isTV = false) shouldBe true
  }

  @Test
  fun `test canRequest4K movie with general REQUEST_4K permission is true`() {
    listOf(ProfilePermission.REQUEST_4K).canRequest4K(isTV = false) shouldBe true
  }

  @Test
  fun `test canRequest4K movie with REQUEST_4K_MOVIE permission is true`() {
    listOf(ProfilePermission.REQUEST_4K_MOVIE).canRequest4K(isTV = false) shouldBe true
  }

  @Test
  fun `test canRequest4K movie with REQUEST_4K_TV permission is false`() {
    listOf(ProfilePermission.REQUEST_4K_TV).canRequest4K(isTV = false) shouldBe false
  }

  @Test
  fun `test canRequest4K movie without permission is false`() {
    emptyList<ProfilePermission>().canRequest4K(isTV = false) shouldBe false
    listOf(ProfilePermission.REQUEST_MOVIE).canRequest4K(isTV = false) shouldBe false
  }

  @Test
  fun `test canRequestAdvanced without permission is false`() {
    emptyList<ProfilePermission>().canRequestAdvanced() shouldBe false
  }

  @Test
  fun `test canRequestAdvanced as admin is true`() {
    listOf(ProfilePermission.ADMIN).canRequestAdvanced() shouldBe true
  }

  @Test
  fun `test canRequestAdvanced with REQUEST_ADVANCED is true`() {
    listOf(ProfilePermission.REQUEST_ADVANCED).canRequestAdvanced() shouldBe true
  }

  @Test
  fun `test canRequestAdvanced with MANAGE_REQUESTS is true`() {
    listOf(ProfilePermission.MANAGE_REQUESTS).canRequestAdvanced() shouldBe true
  }

  @Test
  fun `test isAdmin with ADMIN permission is true`() {
    listOf(ProfilePermission.ADMIN).isAdmin shouldBe true
  }

  @Test
  fun `test isAdmin without ADMIN permission is false`() {
    emptyList<ProfilePermission>().isAdmin shouldBe false
    listOf(ProfilePermission.REQUEST, ProfilePermission.MANAGE_REQUESTS).isAdmin shouldBe false
  }

  @Test
  fun `test multiple permissions including admin`() {
    val permissions = listOf(
      ProfilePermission.ADMIN,
      ProfilePermission.REQUEST,
      ProfilePermission.MANAGE_REQUESTS,
    )

    permissions.canRequest(isTV = true) shouldBe true
    permissions.canRequest(isTV = false) shouldBe true
    permissions.canRequest4K(isTV = true) shouldBe true
    permissions.canRequest4K(isTV = false) shouldBe true
    permissions.canManageRequests() shouldBe true
    permissions.isAdmin shouldBe true
  }

  @Test
  fun `test multiple permissions without admin`() {
    val permissions = listOf(
      ProfilePermission.REQUEST_TV,
      ProfilePermission.REQUEST_4K_MOVIE,
      ProfilePermission.MANAGE_REQUESTS,
    )

    permissions.canRequest(isTV = true) shouldBe true
    permissions.canRequest(isTV = false) shouldBe false
    permissions.canRequest4K(isTV = true) shouldBe false
    permissions.canRequest4K(isTV = false) shouldBe true
    permissions.canManageRequests() shouldBe true
    permissions.isAdmin shouldBe false
  }

  @Test
  fun `test empty permissions list`() {
    val permissions = emptyList<ProfilePermission>()

    permissions.canRequest(isTV = true) shouldBe false
    permissions.canRequest(isTV = false) shouldBe false
    permissions.canRequest4K(isTV = true) shouldBe false
    permissions.canRequest4K(isTV = false) shouldBe false
    permissions.canManageRequests() shouldBe false
    permissions.isAdmin shouldBe false
    permissions.canPerform(ProfilePermission.REQUEST) shouldBe false
  }
}
