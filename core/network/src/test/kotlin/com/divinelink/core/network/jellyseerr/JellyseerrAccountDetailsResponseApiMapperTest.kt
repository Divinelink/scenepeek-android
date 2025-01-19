package com.divinelink.core.network.jellyseerr

import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class JellyseerrAccountDetailsResponseApiMapperTest {

  private val api = JellyseerrAccountDetailsResponseApi(
    id = 1,
    displayName = "Cup10",
    avatar = "/avatarproxy/1dde62cf4a2c436d95e17b9",
    requestCount = 10,
    email = "cup10@proton.me",
    createdAt = "2023-08-19T00:00:00.000Z",
  )

  @Test
  fun `test JellyseerrAccountDetailsResponseApiMapper with valid email`() {
    val result = api.map("http://localhost:5000")

    assertThat(result).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin())
  }

  @Test
  fun `test JellyseerrAccountDetailsResponseApiMapper with invalid email`() {
    val result = api.copy(email = "test.test@me").map("http://localhost:5000")

    assertThat(result).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin().copy(email = null))
  }
}
