package com.andreolas.movierama.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.settings.app.account.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
  private val loginUseCase: LoginUseCase
) : ViewModel() {

  private val _openWebView = MutableSharedFlow<String>()
  val openWebView = _openWebView.asSharedFlow()

  fun login() {
    viewModelScope.launch {

      loginUseCase(Unit)
        .onSuccess {
          _openWebView.emit(it.data.url)
        }.onFailure {
          TODO()
        }
    }
  }
}
