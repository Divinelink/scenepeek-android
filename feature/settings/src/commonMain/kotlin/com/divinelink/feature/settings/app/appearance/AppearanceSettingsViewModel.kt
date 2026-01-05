package com.divinelink.feature.settings.app.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.commons.domain.WhileViewSubscribed
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.designsystem.theme.model.ColorPreference
import com.divinelink.core.designsystem.theme.seedLong
import com.divinelink.core.domain.theme.GetAvailableColorPreferencesUseCase
import com.divinelink.core.domain.theme.GetAvailableThemesUseCase
import com.divinelink.core.domain.theme.GetThemeUseCase
import com.divinelink.core.domain.theme.SetColorPreferenceUseCase
import com.divinelink.core.domain.theme.SetThemeUseCase
import com.divinelink.core.domain.theme.black.backgrounds.GetBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.black.backgrounds.SetBlackBackgroundsUseCase
import com.divinelink.core.domain.theme.color.GetCustomColorUseCase
import com.divinelink.core.domain.theme.color.ObserveColorPreferencesUseCase
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
  val setThemeUseCase: SetThemeUseCase,
  getThemeUseCase: GetThemeUseCase,
  getAvailableThemesUseCase: GetAvailableThemesUseCase,
  val setColorPreferenceUseCase: SetColorPreferenceUseCase,
  getAvailableColorPreferences: GetAvailableColorPreferencesUseCase,
  val setBlackBackgroundsUseCase: SetBlackBackgroundsUseCase,
  observeColorPreferencesUseCase: ObserveColorPreferencesUseCase,
  getBlackBackgroundsUseCase: GetBlackBackgroundsUseCase,
  getCustomColorUseCase: GetCustomColorUseCase,
  val preferenceStorage: PreferenceStorage,
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
      availableColorPreferences = getAvailableColorPreferences(Unit).data,
      blackBackgroundsEnabled = getBlackBackgroundsUseCase(Unit).data,
      colorPreference = observeColorPreferencesUseCase(Unit).first().data,
      customColor = getCustomColorUseCase(Unit).data,
    )
  }.stateIn(
    viewModelScope,
    WhileViewSubscribed,
    initialValue = UpdateSettingsState(
      theme = Theme.SYSTEM,
      availableThemes = listOf(),
      availableColorPreferences = listOf(),
      colorPreference = ColorPreference.Default,
      blackBackgroundsEnabled = false,
      customColor = seedLong,
    ),
  )
  val uiState: StateFlow<UpdateSettingsState> = _uiState

  fun setTheme(theme: Theme) {
    viewModelScope.launch {
      setThemeUseCase(theme)
      refreshData()
    }
  }

  fun setColorPreference(preference: ColorPreference) {
    viewModelScope.launch {
      setColorPreferenceUseCase(preference)
      refreshData()
    }
  }

  fun setBlackBackgrounds(isEnabled: Boolean) {
    viewModelScope.launch {
      setBlackBackgroundsUseCase(isEnabled)
      refreshData()
    }
  }

  fun setCustomColor(color: Long) {
    viewModelScope.launch {
      preferenceStorage.setCustomColor(color)
      refreshData()
    }
  }
}

data class UpdateSettingsState(
  val theme: Theme,
  val colorPreference: ColorPreference,
  val availableThemes: List<Theme>,
  val customColor: Long,
  val availableColorPreferences: List<ColorPreference>,
  val blackBackgroundsEnabled: Boolean,
)
