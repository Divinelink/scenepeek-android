package com.divinelink.feature.settings.app.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.domain.WhileViewSubscribed
import com.divinelink.core.commons.data
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.domain.theme.GetAvailableThemesUseCase
import com.divinelink.core.domain.theme.GetThemeUseCase
import com.divinelink.core.domain.theme.SetThemeUseCase
import com.divinelink.core.domain.theme.black.backgrounds.GetBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.black.backgrounds.SetBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.material.you.GetMaterialYouUseCase
import com.divinelink.core.domain.theme.material.you.GetMaterialYouVisibleUseCase
import com.divinelink.core.domain.theme.material.you.SetMaterialYouUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppearanceSettingsViewModel(
  val setThemeUseCase: SetThemeUseCase,
  getThemeUseCase: GetThemeUseCase,
  getAvailableThemesUseCase: GetAvailableThemesUseCase,
  val setMaterialYouUseCase: SetMaterialYouUseCase,
  getMaterialYouUseCase: GetMaterialYouUseCase,
  val setBlackBackgroundsUseCase: SetBlackBackgroundsUseCase,
  getBlackBackgroundsUseCase: GetBlackBackgroundsUseCase,
  getMaterialYouVisibleUseCase: GetMaterialYouVisibleUseCase,
) : ViewModel() {

  private val refreshSignal = MutableSharedFlow<Unit>()
  private val loadDataSignal: Flow<Unit> = flow {
    emit(Unit)
    emitAll(refreshSignal)
  }

  private suspend fun refreshData() {
    refreshSignal.emit(Unit)
  }

  private val _uiState: StateFlow<UpdateSettingsState> = loadDataSignal.mapLatest {
    UpdateSettingsState(
      theme = getThemeUseCase(Unit).data,
      availableThemes = getAvailableThemesUseCase(Unit).data,
      materialYouEnabled = getMaterialYouUseCase(Unit).data,
      materialYouVisible = getMaterialYouVisibleUseCase(Unit).data,
      blackBackgroundsEnabled = getBlackBackgroundsUseCase(Unit).data,
    )
  }.stateIn(
    viewModelScope,
    WhileViewSubscribed,
    initialValue = UpdateSettingsState(
      theme = Theme.SYSTEM,
      availableThemes = listOf(),
      materialYouEnabled = false,
      materialYouVisible = false,
      blackBackgroundsEnabled = false,
    ),
  )
  val uiState: StateFlow<UpdateSettingsState> = _uiState

  fun setTheme(theme: Theme) {
    viewModelScope.launch {
      setThemeUseCase(theme)
      refreshData()
    }
  }

  fun setMaterialYou(isEnabled: Boolean) {
    viewModelScope.launch {
      setMaterialYouUseCase(isEnabled)
      refreshData()
    }
  }

  fun setBlackBackgrounds(isEnabled: Boolean) {
    viewModelScope.launch {
      setBlackBackgroundsUseCase(isEnabled)
      refreshData()
    }
  }
}

data class UpdateSettingsState(
  val theme: Theme,
  val availableThemes: List<Theme>,
  val materialYouEnabled: Boolean,
  val materialYouVisible: Boolean,
  val blackBackgroundsEnabled: Boolean,
)
