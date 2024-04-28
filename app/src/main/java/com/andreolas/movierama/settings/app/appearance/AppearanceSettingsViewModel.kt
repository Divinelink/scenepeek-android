package com.andreolas.movierama.settings.app.appearance

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.settings.app.appearance.usecase.GetAvailableThemesUseCase
import com.andreolas.movierama.settings.app.appearance.usecase.GetThemeUseCase
import com.andreolas.movierama.settings.app.appearance.usecase.SetThemeUseCase
import com.andreolas.movierama.settings.app.appearance.usecase.material.you.GetMaterialYouUseCase
import com.andreolas.movierama.settings.app.appearance.usecase.material.you.SetMaterialYouUseCase
import com.andreolas.movierama.ui.theme.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.WhileViewSubscribed
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AppearanceSettingsViewModel @Inject constructor(
  val setThemeUseCase: SetThemeUseCase,
  getThemeUseCase: GetThemeUseCase,
  getAvailableThemesUseCase: GetAvailableThemesUseCase,
  val setMaterialYouUseCase: SetMaterialYouUseCase,
  getMaterialYouUseCase: GetMaterialYouUseCase,
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
      materialYouVisible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S // TODO UseCase
    )
  }.stateIn(
    viewModelScope, WhileViewSubscribed,
    initialValue = UpdateSettingsState(
      theme = Theme.SYSTEM,
      availableThemes = listOf(),
      materialYouEnabled = false,
      materialYouVisible = false
    )
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
}

data class UpdateSettingsState(
  val theme: Theme,
  val availableThemes: List<Theme>,
  val materialYouEnabled: Boolean,
  val materialYouVisible: Boolean
)
