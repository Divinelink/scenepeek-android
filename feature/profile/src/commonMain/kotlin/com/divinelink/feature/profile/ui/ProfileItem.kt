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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.coil.AvatarImage
import com.divinelink.core.ui.core_ui_login
import com.divinelink.feature.profile.Res
import com.divinelink.feature.profile.feature_profile_login_description
import com.divinelink.feature.profile.feature_profile_my_profile
import com.valentinilk.shimmer.shimmer
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileItem(
  tmdbAccount: TMDBAccountUiState,
  onLoginClick: () -> Unit,
) {
  AnimatedContent(tmdbAccount) { state ->
    when (state) {
      TMDBAccountUiState.Error -> InitialProfileItem(
        isLoading = false,
        onLoginClick = onLoginClick,
      )
      TMDBAccountUiState.Initial -> InitialProfileItem(
        isLoading = true,
        onLoginClick = onLoginClick,
      )
      is TMDBAccountUiState.LoggedIn -> LoggedInProfileItem(
        state.account,
      )
      TMDBAccountUiState.Anonymous -> InitialProfileItem(
        isLoading = false,
        onLoginClick = onLoginClick,
      )
    }
  }
}

@Composable
private fun LoggedInProfileItem(account: TMDBAccount.LoggedIn) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(MaterialTheme.dimensions.keyline_16),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    AvatarImage.Medium(
      avatarUrl = account.accountDetails.avatarUrl,
      username = account.accountDetails.username,
    )
    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_16))
    Text(
      text = account.accountDetails.username,
      style = MaterialTheme.typography.titleMedium,
    )
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
          text = stringResource(Res.string.feature_profile_my_profile),
          style = MaterialTheme.typography.titleMedium,
        )

        Text(
          text = stringResource(Res.string.feature_profile_login_description),
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
          Text(stringResource(UiString.core_ui_login))
        }
      }
    }
  }
}
