package com.andreolas.movierama.test.util.fakes

import com.andreolas.movierama.ui.ThemedActivityDelegate
import com.divinelink.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeThemedActivityDelegate(
  override val theme: StateFlow<Theme> = MutableStateFlow(Theme.SYSTEM),
  override val currentTheme: Theme = Theme.SYSTEM,
  override val materialYou: StateFlow<Boolean> = MutableStateFlow(false),
  override val blackBackgrounds: StateFlow<Boolean> = MutableStateFlow(false),
) : ThemedActivityDelegate
