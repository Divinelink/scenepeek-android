package com.divinelink.feature.profile.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.profile.R
import com.valentinilk.shimmer.shimmer
import com.divinelink.core.ui.R as uiR

@Composable
fun ProfileItem(
  tmdbAccount: TMDBAccountUiState,
  onLoginClick: () -> Unit,
) {
  AnimatedContent(tmdbAccount) { state ->
    when (state) {
      TMDBAccountUiState.Error -> TODO()
      TMDBAccountUiState.Initial -> InitialProfileItem(
        isLoading = true,
        onLoginClick = onLoginClick,
      )
      is TMDBAccountUiState.LoggedIn -> {
      }
      TMDBAccountUiState.NotLoggedIn -> InitialProfileItem(
        isLoading = false,
        onLoginClick = onLoginClick,
      )
    }
  }
}

@Composable
private fun InitialProfileItem(
  isLoading: Boolean,
  onLoginClick: () -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      Icon(
        imageVector = Icons.Rounded.Person,
        contentDescription = null,
        modifier = Modifier
          .size(MaterialTheme.dimensions.keyline_48)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(MaterialTheme.dimensions.keyline_12),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        Text(
          text = stringResource(R.string.feature_profile_my_profile),
          style = MaterialTheme.typography.titleMedium,
        )

        Text(
          text = stringResource(R.string.feature_profile_login_description),
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
    AnimatedContent(isLoading) { loading ->
      when (loading) {
        true -> Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimensions.keyline_40)
            .shimmer()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.onSurfaceVariant),
        )
        false -> FilledTonalButton(
          modifier = Modifier.fillMaxWidth(),
          onClick = onLoginClick,
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Rounded.Login,
            contentDescription = null,
            modifier = Modifier.size(MaterialTheme.dimensions.keyline_16),
          )
          Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_8))
          Text(stringResource(uiR.string.core_ui_login))
        }
      }
    }
  }
}
