package com.divinelink.core.ui.network

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.UIText
import com.divinelink.core.model.config.ConfigMessage
import com.divinelink.core.ui.components.DisplayMessageDuration
import com.divinelink.core.ui.components.DisplayMessageSection

@Composable
fun AnnouncementPopup(configMessage: ConfigMessage) {
  var visible by rememberSaveable { mutableStateOf(true) }

  AnimatedVisibility(
    visible = !configMessage.dismissable && visible,
  ) {
    DisplayMessageSection(
      modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
      message = DisplayMessage.Success(UIText.StringText(configMessage.content)),
      duration = DisplayMessageDuration.INDEFINITE,
      onTimeout = { visible = false },
    )
  }
}
