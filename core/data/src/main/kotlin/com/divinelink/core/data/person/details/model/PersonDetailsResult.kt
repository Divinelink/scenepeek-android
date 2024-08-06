package com.divinelink.core.data.person.details.model

import com.divinelink.core.model.details.person.PersonDetails

sealed interface PersonDetailsResult {

  data class DetailsSuccess(val personDetails: PersonDetails) : PersonDetailsResult

  data object DetailsFailure : PersonDetailsResult, Exception() {
    private fun readResolve(): Any = DetailsFailure
  }
}
