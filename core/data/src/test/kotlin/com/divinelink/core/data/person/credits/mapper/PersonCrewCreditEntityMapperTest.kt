package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.testing.factories.entity.person.credits.PersonCrewCreditEntityFactory
import com.divinelink.core.testing.factories.model.person.credit.PersonCrewCreditFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class PersonCrewCreditEntityMapperTest {

  @Test
  fun `test crew credit entity mapper`() {
    val entities = PersonCrewCreditEntityFactory.all()

    val domain = PersonCrewCreditFactory.all()

    val mapped = entities.map { it.map() }

    assertThat(mapped).isEqualTo(domain)
  }
}
