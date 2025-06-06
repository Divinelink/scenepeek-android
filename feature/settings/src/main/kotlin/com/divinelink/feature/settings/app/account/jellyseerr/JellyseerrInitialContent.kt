package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.PasswordOutlinedTextField
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.core.ui.R as uiR

@Composable
fun JellyseerrInitialContent(
  modifier: Modifier = Modifier,
  jellyseerrState: JellyseerrState.Initial,
  interaction: (JellyseerrInteraction) -> Unit,
) {
  // 0 = Jellyfin, 1 = Jellyseerr, -1 = None
  var expandedCard by rememberSaveable {
    mutableIntStateOf(jellyseerrState.preferredOption?.ordinal ?: -1)
  }

  Column(
    modifier = modifier
      .verticalScroll(rememberScrollState())
      .fillMaxWidth()
      .fillMaxSize()
      .padding(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      text = stringResource(R.string.feature_settings_jellyseerr_description),
    )

    OutlinedTextField(
      modifier = Modifier
        .testTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .fillMaxWidth(),
      value = jellyseerrState.address,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Uri,
        imeAction = ImeAction.Next,
      ),
      singleLine = true,
      onValueChange = { interaction(JellyseerrInteraction.OnAddressChange(it)) },
      label = { Text(text = stringResource(R.string.feature_settings_jellyseerr_address)) },
    )

    ExpandableCard(
      modifier = Modifier.testTag(TestTags.Settings.Jellyseerr.JELLYFIN_EXPANDABLE_CARD_BUTTON),
      title = stringResource(R.string.feature_settings_login_using_jellyfin),
      isExpanded = expandedCard == 0,
      onExpand = {
        expandedCard = if (expandedCard == 0) {
          -1
        } else {
          0
        }
        interaction(JellyseerrInteraction.OnSelectLoginMethod(JellyseerrAuthMethod.JELLYFIN))
      },
      content = {
        OutlinedTextField(
          modifier = Modifier
            .semantics {
              contentType = ContentType.Username
            }
            .testTag(TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD)
            .fillMaxWidth(),
          value = jellyseerrState.jellyfinLogin.username.value,
          singleLine = true,
          onValueChange = { interaction(JellyseerrInteraction.OnUsernameChange(it)) },
          label = { Text(text = stringResource(R.string.feature_settings_username)) },
        )

        PasswordOutlinedTextField(
          modifier = Modifier
            .semantics {
              contentType = ContentType.Password
            }
            .testTag(TestTags.Settings.Jellyseerr.JELLYFIN_PASSWORD_TEXT_FIELD)
            .fillMaxWidth(),
          value = jellyseerrState.jellyfinLogin.password.value,
          onValueChange = { interaction(JellyseerrInteraction.OnPasswordChange(it)) },
        )
      },
    )

    ExpandableCard(
      modifier = Modifier.testTag(TestTags.Settings.Jellyseerr.JELLYSEERR_EXPANDABLE_CARD_BUTTON),
      title = stringResource(R.string.feature_settings_login_using_jellyseerr),
      isExpanded = expandedCard == 1,
      onExpand = {
        expandedCard = if (expandedCard == 1) {
          -1
        } else {
          1
        }
        interaction(JellyseerrInteraction.OnSelectLoginMethod(JellyseerrAuthMethod.JELLYSEERR))
      },
      content = {
        OutlinedTextField(
          modifier = Modifier
            .semantics {
              contentType = ContentType.EmailAddress
            }
            .testTag(TestTags.Settings.Jellyseerr.JELLYSEERR_USERNAME_TEXT_FIELD)
            .fillMaxWidth(),
          value = jellyseerrState.jellyseerrLogin.username.value,
          singleLine = true,
          onValueChange = { interaction(JellyseerrInteraction.OnUsernameChange(it)) },
          label = { Text(text = stringResource(R.string.feature_settings_username_or_email)) },
        )

        PasswordOutlinedTextField(
          modifier = Modifier
            .semantics {
              contentType = ContentType.Password
            }
            .testTag(TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD)
            .fillMaxWidth(),
          value = jellyseerrState.jellyseerrLogin.password.value,
          onValueChange = { interaction(JellyseerrInteraction.OnPasswordChange(it)) },
        )
      },
    )

    Spacer(Modifier.height(MaterialTheme.dimensions.keyline_32))

    AnimatedContent(
      targetState = jellyseerrState.isLoading,
      contentAlignment = Alignment.Center,
      label = "Login Button Animated Content",
    ) { loading ->
      when (loading) {
        true -> Row(
          modifier = Modifier
            .testTag(TestTags.LOADING_PROGRESS)
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
        ) {
          CircularProgressIndicator()
        }
        false -> Button(
          modifier = Modifier
            .testTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON)
            .fillMaxWidth(),
          enabled = jellyseerrState.isLoginEnabled,
          onClick = { interaction(JellyseerrInteraction.OnLoginClick) },
        ) {
          Text(stringResource(id = uiR.string.core_ui_login))
        }
      }
    }
  }
}

@Composable
fun ExpandableCard(
  modifier: Modifier = Modifier,
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
        modifier = modifier
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
    AnimatedVisibility(visible = isExpanded) {
      Column {
        content()
      }
    }
  }
}

@Previews
@Composable
private fun JellyseerrBottomSheetContentPreview() {
  AppTheme {
    Surface {
      JellyseerrInitialContent(
        jellyseerrState = JellyseerrState.Initial(
          address = "http://localhost:8096",
          isLoading = false,
          preferredOption = null,
        ),
        interaction = {},
      )
    }
  }
}

@Previews
@Composable
private fun JellyseerrBottomSheetContentLoadingPreview() {
  AppTheme {
    Surface {
      JellyseerrInitialContent(
        jellyseerrState = JellyseerrState.Initial(
          address = "http://localhost:8096",
          isLoading = true,
          preferredOption = null,
        ),
        interaction = {},
      )
    }
  }
}
