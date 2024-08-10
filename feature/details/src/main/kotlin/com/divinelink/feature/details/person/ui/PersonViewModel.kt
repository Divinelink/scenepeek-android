package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import com.divinelink.core.domain.details.person.PersonDetailsParams
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
  fetchPersonDetailsUseCase: FetchPersonDetailsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val args: PersonNavArguments = PersonScreenDestination.argsFrom(savedStateHandle)

  private val _uiState: MutableStateFlow<PersonUiState> = MutableStateFlow(
    PersonUiState.Success(
      personDetails = PersonDetailsUiState.Visible(
        personDetails = PersonDetails(
          person = Person(
            id = args.id,
            name = args.name ?: "",
            profilePath = args.profilePath,
            gender = args.gender ?: Gender.NOT_SET,
            knownForDepartment = args.knownForDepartment,
            role = PersonRole.Unknown,
          ),
          biography = null,
          birthday = null,
          deathday = null,
          placeOfBirth = null,
          homepage = null,
          alsoKnownAs = emptyList(),
          imdbId = null,
          popularity = 0.0,
        ),
      ),
    ),
  )
  val uiState: StateFlow<PersonUiState> = _uiState.asStateFlow()

  init {
    fetchPersonDetailsUseCase(
      PersonDetailsParams(
        id = args.id,
        knownForDepartment = args.knownForDepartment,
      ),
    )
      .onEach { result ->
        result.fold(
          onSuccess = { detailsResult ->
            when (detailsResult) {
              is PersonDetailsResult.DetailsSuccess -> _uiState.update { uiState ->
                uiState.updatePersonDetails(detailsResult.personDetails)
              }

              is PersonDetailsResult.CreditsSuccess -> _uiState.update { uiState ->
                uiState.updateCredits(knownFor = detailsResult.knownForCredits)
              }

              is PersonDetailsResult.DetailsFailure -> _uiState.update { PersonUiState.Error }
            }
          },
          onFailure = {
            Timber.d(it)
            _uiState.update { PersonUiState.Error }
          },
        )
      }
      .launchIn(viewModelScope)
  }
}
