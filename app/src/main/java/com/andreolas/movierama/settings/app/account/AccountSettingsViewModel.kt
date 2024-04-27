package com.andreolas.movierama.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.settings.app.account.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
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
      when (val result = loginUseCase(Unit)) {
        is Result.Success -> {
          _openWebView.emit(result.data.data!!)
        }
        is Result.Error -> TODO()
        Result.Loading -> TODO()
      }
    }
  }
}
