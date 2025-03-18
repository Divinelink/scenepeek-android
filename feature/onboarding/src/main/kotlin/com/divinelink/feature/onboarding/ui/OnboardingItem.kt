package com.divinelink.feature.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.getString
import com.divinelink.feature.onboarding.OnboardingAction
import com.divinelink.feature.onboarding.OnboardingPage
import com.divinelink.feature.onboarding.OnboardingPages
import com.divinelink.feature.onboarding.R

@Composable
fun OnboardingItem(
  page: OnboardingPage,
  onActionClick: (OnboardingAction) -> Unit,
  isLast: Boolean,
  onCompleteOnboarding: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Spacer(modifier = Modifier.height(64.dp))
    page.image?.let {
      Image(
        modifier = Modifier
          .fillMaxWidth()
          .size(168.dp),
        painter = painterResource(id = it),
        contentDescription = null,
      )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.dimensions.keyline_24),
      textAlign = TextAlign.Center,
      text = page.title.getString(),
      style = MaterialTheme.typography.titleLarge,
    )

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.dimensions.keyline_24),
      textAlign = TextAlign.Center,
      text = page.description.getString(),
      style = MaterialTheme.typography.bodyLarge,
    )

    Spacer(modifier = Modifier.weight(1f))

    Column(
      modifier = Modifier
        .padding(horizontal = MaterialTheme.dimensions.keyline_16)
        .padding(
          bottom = LocalBottomNavigationPadding.current + MaterialTheme.dimensions.keyline_64,
        ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      if (isLast) {
        OutlinedButton(
          modifier = Modifier.fillMaxWidth(),
          onClick = onCompleteOnboarding,
        ) {
          Text(text = stringResource(R.string.feature_onboarding_get_started))
        }
      }

      page.action?.let { action ->
        if (action.isComplete) {
          SuccessText(action.completedActionText)
        } else {
          Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onActionClick.invoke(action) },
          ) {
            Text(text = action.actionText.getString())
          }
        }
      }
    }
  }
}

@Previews
@Composable
fun OnboardingItemPreview() {
  AppTheme {
    Surface {
      OnboardingItem(
        page = OnboardingPages.initialPages.last(),
        onActionClick = {},
        onCompleteOnboarding = {},
        isLast = true,
      )
    }
  }
}
