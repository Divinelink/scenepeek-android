package com.divinelink.core.database

import com.divinelink.core.database.jellyseerr.mapper.map
import com.divinelink.core.database.jellyseerr.mapper.mapToEntity
import com.divinelink.core.testing.factories.entity.JellyseerrAccountDetailsEntityFactory
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class JellyseerrAccountDetailsEntityMapperTest {

  @Test
  fun `test JellyseerrAccountDetails mapToEntity`() {
    val accountDetails = listOf(
      JellyseerrAccountDetailsFactory.jellyfin(),
      JellyseerrAccountDetailsFactory.jellyseerr(),
    )

    val mapped = accountDetails.map {
      it.mapToEntity()
    }

    val entities = listOf(
      JellyseerrAccountDetailsEntityFactory.jellyfin(),
      JellyseerrAccountDetailsEntityFactory.jellyseerr().copy(avatar = ""),
    )

    assertThat(mapped).isEqualTo(entities)
  }

  @Test
  fun `test JellyseerrAccountDetailsEntity map`() {
    val entities = listOf(
      JellyseerrAccountDetailsEntityFactory.jellyfin(),
      JellyseerrAccountDetailsEntityFactory.jellyseerr(),
    )

    val mapped = entities.map(JellyseerrAccountDetailsEntity::map)

    val accountDetails = listOf(
      JellyseerrAccountDetailsFactory.jellyfin(),
      JellyseerrAccountDetailsFactory.jellyseerr(),
    )

    assertThat(mapped).isEqualTo(accountDetails)
  }
}
