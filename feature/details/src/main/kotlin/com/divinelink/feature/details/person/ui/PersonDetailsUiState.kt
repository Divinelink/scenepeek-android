package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails

sealed interface PersonDetailsUiState {
  data object Loading : PersonDetailsUiState

  sealed class Data(open val personDetails: PersonDetails) : PersonDetailsUiState {

    data class Prefetch(val person: Person) : Data(PersonDetails.initial(person = person))
    data class Visible(override val personDetails: PersonDetails) : Data(personDetails)
  }
}
