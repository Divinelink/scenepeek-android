package com.andreolas.movierama.session.login.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.destinations.LoginWebViewScreenDestination
import com.andreolas.movierama.session.login.domain.token.usecase.HandleAuthenticationRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginWebViewViewModel @Inject constructor(
  private val handleAuthenticationRequestUseCase: HandleAuthenticationRequestUseCase,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val args: LoginScreenArgs = LoginWebViewScreenDestination.argsFrom(savedStateHandle)

  private val _viewState = MutableStateFlow(LoginViewState.initial(args.requestToken))
  val viewState: StateFlow<LoginViewState> = _viewState

  fun handleSession(url: String) {
    viewModelScope.launch {
      handleAuthenticationRequestUseCase(url)
        .onSuccess {
          _viewState.update {
            it.copy(navigateBack = true)
          }
        }
        .onFailure {
          _viewState.update {
            it.copy(navigateBack = true)
          }
        }
    }
  }
}
