package com.divinelink.feature.onboarding.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.onboarding.Res
import com.divinelink.feature.onboarding.feature_onboarding_get_started
import org.jetbrains.compose.resources.stringResource

@Composable
fun SkipIntroButton(onClick: () -> Unit) {
  Column(
    modifier = Modifier
      .padding(top = MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    TextButton(
      onClick = onClick,
    ) {
      Text(stringResource(Res.string.feature_onboarding_get_started))
    }
  }
}
