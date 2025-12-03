package com.divinelink.core.network.jellyseerr

import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.JellyseerrProfileResponse
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class JellyseerrAccountDetailsResponseApiMapperTest {

  private val api = JellyseerrProfileResponse(
    id = 1,
    displayName = "Cup10",
    avatar = "/avatarproxy/1dde62cf4a2c436d95e17b9",
    requestCount = 10,
    email = "cup10@proton.me",
    createdAt = "2023-08-19T00:00:00.000Z",
    permissions = 7082016,
  )

  @Test
  fun `test JellyseerrAccountDetailsResponseApiMapper with valid email`() {
    val result = api.map("http://localhost:5000")

    assertThat(result).isEqualTo(JellyseerrProfileFactory.jellyfin())
  }

  @Test
  fun `test JellyseerrAccountDetailsResponseApiMapper with invalid email`() {
    val result = api.copy(email = "test.test@me").map("http://localhost:5000")

    assertThat(result).isEqualTo(JellyseerrProfileFactory.jellyfin().copy(email = null))
  }
}
