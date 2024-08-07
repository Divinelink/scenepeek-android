package com.divinelink.core.data.person.details.model

import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import com.divinelink.core.model.person.credits.PersonCredit

sealed interface PersonDetailsResult {

  data class DetailsSuccess(val personDetails: PersonDetails) : PersonDetailsResult

  data class CreditsSuccess(
    val knownForCredits: List<PersonCredit>,
    val credits: PersonCombinedCredits,
  ) : PersonDetailsResult

  data object DetailsFailure : PersonDetailsResult, Exception() {
    private fun readResolve(): Any = DetailsFailure
  }
}
