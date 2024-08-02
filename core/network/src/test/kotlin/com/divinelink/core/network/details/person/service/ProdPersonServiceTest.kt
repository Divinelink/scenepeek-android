package com.divinelink.core.network.details.person.service

import com.divinelink.core.network.details.person.model.PersonDetailsApi
import com.divinelink.core.testing.factories.api.details.person.PersonDetailsApiFactory
import com.divinelink.core.testing.network.TestRestClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdPersonServiceTest {

  private lateinit var service: ProdPersonService
  private lateinit var restClient: TestRestClient

  @BeforeTest
  fun setUp() {
    restClient = TestRestClient()
  }

  @Test
  fun `test fetch person details by id`() = runTest {
    restClient.mockGetResponse<PersonDetailsApi>(
      url = "https://api.themoviedb.org/3/person/4495",
      jsonFileName = "person-details.json",
    )

    service = ProdPersonService(restClient.restClient)

    val person = service.fetchPersonDetails(id = 4495).single()

    assertThat(person).isEqualTo(PersonDetailsApiFactory.steveCarell())
  }
}
