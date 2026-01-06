package com.divinelink.feature.settings.app.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.commons.domain.WhileViewSubscribed
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.domain.theme.GetAvailableColorSystemsUseCase
import com.divinelink.core.domain.theme.GetAvailableThemesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppearanceSettingsViewModel(
  private val preferencesRepository: PreferencesRepository,
  getAvailableThemesUseCase: GetAvailableThemesUseCase,
  getAvailableColorPreferences: GetAvailableColorSystemsUseCase,
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
      themePreferences = preferencesRepository.themePreferences.first(),
      availableThemes = getAvailableThemesUseCase(Unit).data,
      availableColorSystems = getAvailableColorPreferences(Unit).data,
    )
  }.stateIn(
    scope = viewModelScope,
    started = WhileViewSubscribed,
    initialValue = UpdateSettingsState(
      themePreferences = ThemePreferences.initial,
      availableThemes = listOf(),
      availableColorSystems = listOf(),
    ),
  )
  val uiState: StateFlow<UpdateSettingsState> = _uiState

  fun setTheme(theme: Theme) {
    viewModelScope.launch {
      preferencesRepository.updateCurrentTheme(theme)
      refreshData()
    }
  }

  fun updateColorSystem(system: ColorSystem) {
    viewModelScope.launch {
      preferencesRepository.updateColorSystem(system)
      refreshData()
    }
  }

  fun setBlackBackgrounds(isEnabled: Boolean) {
    viewModelScope.launch {
      preferencesRepository.setPureBlack(isEnabled)
      refreshData()
    }
  }

  fun setThemeColor(color: Long) {
    viewModelScope.launch {
      preferencesRepository.updateThemeColor(color)
      refreshData()
    }
  }
}

data class UpdateSettingsState(
  val themePreferences: ThemePreferences,
  val availableThemes: List<Theme>,
  val availableColorSystems: List<ColorSystem>,
)
