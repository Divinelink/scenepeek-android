package com.divinelink.core.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.model.ScreenType
import com.divinelink.core.model.shareUrl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DropdownMenuViewModel(
  entryPoint: ScreenType,
  private val spoilersObfuscationUseCase: SpoilersObfuscationUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<DropDownMenuUiState> = MutableStateFlow(
    DropDownMenuUiState.initial(
      entryPoint = entryPoint,
    ),
  )
  val uiState: StateFlow<DropDownMenuUiState> = _uiState

  private val _shareUrl = Channel<String>()
  val shareUrl: Flow<String> = _shareUrl.receiveAsFlow()

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
    }
  }
}
