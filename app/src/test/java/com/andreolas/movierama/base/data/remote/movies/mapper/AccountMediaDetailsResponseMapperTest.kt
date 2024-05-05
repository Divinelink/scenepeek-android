package com.andreolas.movierama.base.data.remote.movies.mapper

import com.andreolas.factories.api.account.states.AccountMediaDetailsResponseApiFactory
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.google.common.truth.Truth.assertThat
import org.junit.Test

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
