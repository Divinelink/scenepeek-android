package com.andreolas.movierama.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.settings.app.account.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
  private val loginUseCase: LoginUseCase
) : ViewModel() {

  private val _viewState = MutableStateFlow(AccountSettingsViewState(url = null))
  val viewState: StateFlow<AccountSettingsViewState> = _viewState

  fun login() {
    viewModelScope.launch {
      loginUseCase(Unit)
        .onSuccess { response ->
          _viewState.update {
            it.copy(url = response.data.url)
          }
        }.onFailure {
          TODO()
        }
    }
  }

  fun onWebViewScreenNavigated() {
    _viewState.update {
      it.copy(url = null)
    }
  }
}

data class AccountSettingsViewState(
  val url: String?
)
