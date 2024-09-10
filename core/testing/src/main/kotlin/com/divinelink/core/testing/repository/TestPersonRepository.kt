package com.divinelink.core.testing.repository

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.model.change.Changes
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
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

  fun mockFetchChanges(response: Result<Changes>) {
    whenever(mock.fetchPersonChanges(any(), any())).thenReturn(flowOf(response))
    times(2)
  }

  fun verifyExecuteFetchChanges(invocations: Int = 1) {
    verify(mock, times(invocations)).fetchPersonChanges(any(), any())
  }

  fun verifyUpdatePerson(
    id: Long,
    biography: String? = null,
    name: String? = null,
    birthday: String? = null,
    deathday: String? = null,
    gender: Int? = null,
    homepage: String? = null,
    imdbId: String? = null,
    knownForDepartment: String? = null,
    placeOfBirth: String? = null,
    profilePath: String? = null,
    insertedAt: String? = null,
  ) = verify(mock).updatePerson(
    id = id,
    biography = biography,
    name = name,
    birthday = birthday,
    deathday = deathday,
    gender = gender,
    homepage = homepage,
    imdbId = imdbId,
    knownForDepartment = knownForDepartment,
    placeOfBirth = placeOfBirth,
    profilePath = profilePath,
    insertedAt = insertedAt,
  )

  fun verifyNoUpdatesForPerson() {
    verify(mock, never()).fetchPersonChanges(any(), any())
    verify(mock, never()).updatePerson(
      id = any(),
      biography = any(),
      name = any(),
      birthday = any(),
      deathday = any(),
      gender = any(),
      homepage = any(),
      imdbId = any(),
      knownForDepartment = any(),
      placeOfBirth = any(),
      profilePath = any(),
      insertedAt = any(),
    )
  }
}
