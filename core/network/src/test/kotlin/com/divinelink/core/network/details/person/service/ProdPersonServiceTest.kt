package com.divinelink.core.network.details.person.service

import com.divinelink.core.network.details.person.model.PersonCreditsApi
import com.divinelink.core.network.details.person.model.PersonDetailsApi
import com.divinelink.core.testing.factories.api.details.person.PersonCastCreditApiFactory
import com.divinelink.core.testing.factories.api.details.person.PersonCrewCreditApiFactory
import com.divinelink.core.testing.factories.api.details.person.PersonDetailsApiFactory
import com.divinelink.core.testing.network.TestRestClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdPersonServiceTest {

  private lateinit var service: ProdPersonService
  private lateinit var testRestClient: TestRestClient

  @BeforeTest
  fun setUp() {
    testRestClient = TestRestClient()
  }

  @Test
  fun `test fetch person details by id`() = runTest {
    testRestClient.mockGetResponse<PersonDetailsApi>(
      url = "https://api.themoviedb.org/3/person/4495",
      jsonFileName = "person-details.json",
    )

    service = ProdPersonService(testRestClient.restClient)

    val person = service.fetchPersonDetails(id = 4495).single()

    assertThat(person).isEqualTo(PersonDetailsApiFactory.steveCarell())
  }

  @Test
  fun `test fetch person combined credits`() = runTest {
    testRestClient.mockGetResponse<PersonCreditsApi>(
      url = "https://api.themoviedb.org/3/person/4495/combined_credits",
      jsonFileName = "person-combined-credits.json",
    )

    service = ProdPersonService(testRestClient.restClient)

    val credits = service.fetchPersonCombinedCredits(id = 4495).single()

    assertThat(credits.id).isEqualTo(4495)
    assertThat(credits.cast.subList(0, 4)).isEqualTo(PersonCastCreditApiFactory.all())
    assertThat(credits.crew.subList(0, 4)).isEqualTo(PersonCrewCreditApiFactory.all())
  }
}
