package com.divinelink.scenepeek.base.data.remote.movies.mapper

import com.divinelink.core.data.details.mapper.map
import com.divinelink.factories.api.account.states.AccountMediaDetailsResponseApiFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class AccountMediaDetailsResponseMapperTest {

  @Test
  fun `test rated map to domain`() {
    val response = AccountMediaDetailsResponseApiFactory.Rated()
    val domain = AccountMediaDetailsFactory.Rated()

    val mapped = response.map()

    assertThat(mapped).isEqualTo(domain)
  }

  @Test
  fun `test not rated map to domain`() {
    val response = AccountMediaDetailsResponseApiFactory.NotRated()
    val domain = AccountMediaDetailsFactory.NotRated()

    val mapped = response.map()

    assertThat(mapped).isEqualTo(domain)
  }
}
