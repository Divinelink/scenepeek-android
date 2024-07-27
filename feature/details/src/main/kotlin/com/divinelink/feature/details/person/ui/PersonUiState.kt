package com.divinelink.feature.details.person.ui

data class PersonUiState(val personId: Long) {
  companion object {
    fun initial(id: Long): PersonUiState = PersonUiState(personId = id)
  }
}
