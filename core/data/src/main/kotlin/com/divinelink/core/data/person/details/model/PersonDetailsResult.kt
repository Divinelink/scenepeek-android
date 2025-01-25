package com.divinelink.core.data.person.details.model

import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCredit

sealed interface PersonDetailsResult {

  data class DetailsSuccess(val personDetails: PersonDetails) : PersonDetailsResult

  data class CreditsSuccess(
    val knownForCredits: List<PersonCredit>,
    val knownForDepartment: String,
    val movies: GroupedPersonCredits,
    val tvShows: GroupedPersonCredits,
  ) : PersonDetailsResult

  data object DetailsFailure : PersonDetailsResult, Exception() {
    private fun readResolve(): Any = DetailsFailure
  }
}
