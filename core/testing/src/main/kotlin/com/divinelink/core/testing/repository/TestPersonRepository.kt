package com.divinelink.core.testing.repository

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.model.details.person.PersonDetails
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestPersonRepository {

  val mock: PersonRepository = mock()

  fun mockFetchPersonDetails(response: Result<PersonDetails>) {
    whenever(mock.fetchPersonDetails(any())).thenReturn(flowOf(response))
  }

  fun mockFetchPersonException(exception: Exception) {
    whenever(mock.fetchPersonDetails(any())).thenThrow(exception::class.java)
  }
}
