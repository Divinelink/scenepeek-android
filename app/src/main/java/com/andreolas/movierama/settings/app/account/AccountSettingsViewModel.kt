package com.andreolas.movierama.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.session.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
  private val repository: SessionRepository
) : ViewModel() {

  fun login() {
    viewModelScope.launch {
      // todo
    }
  }
}
