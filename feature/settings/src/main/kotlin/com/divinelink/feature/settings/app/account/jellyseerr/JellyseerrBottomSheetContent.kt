package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.Previews
import com.divinelink.feature.settings.R
import com.divinelink.core.ui.R as uiR

@Composable
fun JellyseerrBottomSheetContent(
  jellyseerrState: JellyseerrState.Initial,
  interaction: (JellyseerrInteraction) -> Unit,
) {
  // 0 = Jellyfin, 1 = Jellyseerr,
  var expandedCard by rememberSaveable {
    mutableStateOf(jellyseerrState.preferredOption?.ordinal)
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxSize()
      .padding(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      text = stringResource(R.string.feature_settings_jellyseerr_description),
    )

    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = jellyseerrState.address,
      onValueChange = { interaction(JellyseerrInteraction.OnAddressChange(it)) },
      label = { Text(text = stringResource(R.string.feature_settings_address)) },
    )

    ExpandableCard(
      title = stringResource(R.string.feature_settings_login_using_jellyfin),
      isExpanded = expandedCard == 0,
      onExpand = {
        expandedCard = if (expandedCard == 0) {
          null
        } else {
          0
        }
        interaction(JellyseerrInteraction.OnSelectLoginMethod(JellyseerrLoginMethod.JELLYFIN))
      },
      content = {
        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          value = jellyseerrState.jellyfinLogin.username.value,
          onValueChange = { interaction(JellyseerrInteraction.OnUsernameChange(it)) },
          label = { Text(text = stringResource(R.string.feature_settings_username)) },
        )

        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          value = jellyseerrState.jellyfinLogin.password.value,
          onValueChange = { interaction(JellyseerrInteraction.OnPasswordChange(it)) },
          label = { Text(text = stringResource(R.string.feature_settings_password)) },
        )
      },
    )

    ExpandableCard(
      title = stringResource(R.string.feature_settings_login_using_jellyseerr),
      isExpanded = expandedCard == 1,
      onExpand = {
        expandedCard = if (expandedCard == 1) {
          null
        } else {
          1
        }
        interaction(JellyseerrInteraction.OnSelectLoginMethod(JellyseerrLoginMethod.JELLYSEERR))
      },
      content = {
        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          value = jellyseerrState.jellyseerrLogin.username.value,
          onValueChange = { interaction(JellyseerrInteraction.OnUsernameChange(it)) },
          label = { Text(text = stringResource(R.string.feature_settings_username_or_email)) },
        )

        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          value = jellyseerrState.jellyseerrLogin.password.value,
          onValueChange = { interaction(JellyseerrInteraction.OnPasswordChange(it)) },
          label = { Text(text = stringResource(R.string.feature_settings_password)) },
        )
      },
    )

    Spacer(Modifier.height(MaterialTheme.dimensions.keyline_32))

    Button(
      modifier = Modifier.fillMaxWidth(),
      enabled = jellyseerrState.isLoginEnabled,
      onClick = { interaction(JellyseerrInteraction.OnLoginClick) },
    ) {
      Text(stringResource(id = uiR.string.core_ui_login))
    }
  }
}

@Composable
fun ExpandableCard(
  title: String,
  isExpanded: Boolean,
  onExpand: () -> Unit,
  content: @Composable () -> Unit,
) {
  Column(
    modifier = Modifier
      .clickable {
        onExpand()
      }
      .fillMaxWidth()
      .animateContentSize(
        animationSpec = tween(
          durationMillis = 300,
          easing = LinearOutSlowInEasing,
        ),
      )
      .padding(MaterialTheme.dimensions.keyline_8),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        modifier = Modifier.weight(6f),
        text = title,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      IconButton(
        modifier = Modifier
          .weight(1f)
          .alpha(0.2f)
          .rotate(if (isExpanded) 180f else 0f),
        onClick = {
          onExpand()
        },
      ) {
        Icon(
          imageVector = Icons.Default.ArrowDropDown,
          contentDescription = "Drop-Down Arrow",
        )
      }
    }
    if (isExpanded) {
      content()
    }
  }
}

@Previews
@Composable
private fun JellyseerrBottomSheetContentPreview() {
  AppTheme {
    Surface {
      JellyseerrBottomSheetContent(
        jellyseerrState = JellyseerrState.Initial(null),
        interaction = {},
      )
    }
  }
}
