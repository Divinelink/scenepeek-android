package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.testing.factories.entity.person.credits.PersonCastCreditEntityFactory
import com.divinelink.core.testing.factories.model.person.credit.PersonCastCreditFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class PersonCastCreditEntityMapperTest {

  @Test
  fun `test cast credit entity mapper`() {
    val entities = PersonCastCreditEntityFactory.all()

    val domain = PersonCastCreditFactory.all()

    val mapped = entities.map { it.map() }

    assertThat(mapped).isEqualTo(domain)
  }
}
