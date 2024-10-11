package com.divinelink.core.network.jellyseerr

import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class JellyseerrAccountDetailsResponseApiMapperTest {

  private val api = JellyseerrAccountDetailsResponseApi(
    id = 1,
    displayName = "Cup10",
    avatar = "http://localhost:5000/avatar",
    requestCount = 10,
    email = "cup10@proton.me",
    createdAt = "2023-08-19T00:00:00.000Z",
  )

  @Test
  fun `test JellyseerrAccountDetailsResponseApiMapper with valid email`() {
    val result = api.map()

    assertThat(result).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin())
  }

  @Test
  fun `test JellyseerrAccountDetailsResponseApiMapper with invalid email`() {
    val result = api.copy(email = "test.test@me").map()

    assertThat(result).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin().copy(email = null))
  }
}
