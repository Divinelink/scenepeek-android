package com.andreolas.movierama.settings

import androidx.lifecycle.ViewModel
import com.andreolas.movierama.ui.theme.ThemedActivityDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  themedActivityDelegate: ThemedActivityDelegate,
) : ViewModel(), ThemedActivityDelegate by themedActivityDelegate
