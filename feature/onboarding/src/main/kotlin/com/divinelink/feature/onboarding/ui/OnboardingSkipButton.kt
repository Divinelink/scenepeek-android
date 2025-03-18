package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.onboarding.R

@Composable
fun OnboardingSkipButton(onClick: () -> Unit) {
  Row(
    Modifier
      .padding(WindowInsets.statusBars.asPaddingValues())
      .fillMaxWidth(),
  ) {
    Spacer(Modifier.weight(1f))

    TextButton(
      modifier = Modifier.padding(end = MaterialTheme.dimensions.keyline_16),
      onClick = onClick,
    ) {
      Text(stringResource(R.string.feature_onboarding_skip))
    }
  }
}
