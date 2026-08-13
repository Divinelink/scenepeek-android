package com.divinelink.core.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.opendata.OpenDataRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.model.ScreenType
import com.divinelink.core.model.UIText
import com.divinelink.core.model.shareUrl
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_open_in_error_message
import com.divinelink.core.ui.snackbar.SnackbarMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DropdownMenuViewModel(
  entryPoint: ScreenType,
  private val spoilersObfuscationUseCase: SpoilersObfuscationUseCase,
  private val openDataRepository: OpenDataRepository,
  private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<DropDownMenuUiState> = MutableStateFlow(
    DropDownMenuUiState.initial(
      entryPoint = entryPoint,
    ),
  )
  val uiState: StateFlow<DropDownMenuUiState> = _uiState

  private val _shareUrl = Channel<String>()
  val shareUrl: Flow<String> = _shareUrl.receiveAsFlow()

  private val _openUrl = Channel<String>()
  val openUrl: Flow<String> = _openUrl.receiveAsFlow()

  fun updateEntryPoint(entryPoint: ScreenType) {
    _uiState.update { it.copy(entryPoint = entryPoint) }
  }

  fun onAction(intent: DropdownMenuIntent) {
    when (intent) {
      DropdownMenuIntent.Share -> viewModelScope.launch {
        _shareUrl.send(
          _uiState.value.entryPoint.shareUrl(
            domain = "themoviedb.org",
            name = _uiState.value.entryPoint.name,
          ),
        )
      }
      is DropdownMenuIntent.ShowOrHideSpoilers -> viewModelScope.launch {
        spoilersObfuscationUseCase.setSpoilersObfuscation()
      }
      is DropdownMenuIntent.OpenInWikipedia -> viewModelScope.launch {
        openDataRepository
          .getWikipediaTitle(intent.title)
          .fold(
            onSuccess = { wikis ->
              val preferredLocale = preferencesRepository.metadataLanguage.first().code
              val resolvedLocale = if (wikis.containsKey(preferredLocale)) preferredLocale else "en"
              val wikiTitle = wikis[resolvedLocale]

              if (wikiTitle != null) {
                _openUrl.send("https://$resolvedLocale.wikipedia.org/wiki/$wikiTitle")
              } else {
                _uiState.update {
                  it.copy(
                    snackbarMessage = SnackbarMessage.from(
                      UIText.ResourceText(UiString.core_ui_open_in_error_message),
                    ),
                  )
                }
              }
            },
            onFailure = {
              _uiState.update {
                it.copy(
                  snackbarMessage = SnackbarMessage.from(
                    UIText.ResourceText(UiString.core_ui_open_in_error_message),
                  ),
                )
              }
            },
          )
      }
    }
  }

  fun consumeSnackbarMessage() {
    _uiState.update { it.copy(snackbarMessage = null) }
  }
}
