package com.divinelink.core.testing.repository

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestPersonRepository {

  val mock: PersonRepository = mock()

  fun mockFetchPersonDetails(response: Result<PersonDetails>) {
    whenever(mock.fetchPersonDetails(any())).thenReturn(flowOf(response))
  }

  fun mockFetchPersonDetails(response: Channel<Result<PersonDetails>>) {
    whenever(mock.fetchPersonDetails(any())).thenReturn(response.consumeAsFlow())
  }

  fun mockFetchPersonCredits(response: Result<PersonCombinedCredits>) {
    whenever(mock.fetchPersonCredits(any())).thenReturn(flowOf(response))
  }
}
