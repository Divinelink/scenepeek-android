package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.PasswordOutlinedTextField
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.app.account.jellyseerr.preview.JellyseerrLoginStatePreviewParameterProvider
import com.divinelink.feature.settings.app.account.jellyseerr.ui.EmailSupportField
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_email
import com.divinelink.feature.settings.resources.feature_settings_jellyseerr_address_placeholder
import com.divinelink.feature.settings.resources.feature_settings_jellyseerr_description
import com.divinelink.feature.settings.resources.feature_settings_jellyseerr_server_url
import com.divinelink.feature.settings.resources.feature_settings_sign_in_with
import com.divinelink.feature.settings.resources.feature_settings_signing_in
import com.divinelink.feature.settings.resources.feature_settings_username
import org.jetbrains.compose.resources.stringResource

@Composable
fun JellyseerrLoginContent(
  modifier: Modifier = Modifier,
  state: JellyseerrState.Login,
  interaction: (JellyseerrInteraction) -> Unit,
) {
  ScenePeekLazyColumn(
    contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_16),
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    item {
      Text(
        text = stringResource(Res.string.feature_settings_jellyseerr_description),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
      )
    }

    item {
      OutlinedTextField(
        modifier = Modifier
          .testTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
          .fillMaxWidth(),
        value = state.loginData.address.value,
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color(0xFF6366F1),
          focusedLabelColor = Color(0xFF6366F1),
          cursorColor = Color(0xFF6366F1),
        ),
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Uri,
          imeAction = ImeAction.Next,
        ),
        singleLine = true,
        onValueChange = { interaction(JellyseerrInteraction.OnAddressChange(it)) },
        label = { Text(text = stringResource(Res.string.feature_settings_jellyseerr_server_url)) },
        placeholder = {
          Text(text = stringResource(Res.string.feature_settings_jellyseerr_address_placeholder))
        },
      )
    }

    item {
      AuthMethodSelector(
        selectedMethod = state.loginData.authMethod,
        onMethodSelected = { interaction.invoke(JellyseerrInteraction.OnSelectLoginMethod(it)) },
      )
    }

    item {
      LoginForm(
        state = state,
        interaction = interaction,
      )
    }

    item {
      LoginButton(
        selectedMethod = state.loginData.authMethod,
        isLoading = state.isLoading,
        enabled = state.isLoginEnabled,
        onClick = { interaction.invoke(JellyseerrInteraction.OnLoginClick) },
      )
    }

    item {
      EmailSupportField()
    }

    item {
      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_96))
    }
  }
}

@Composable
private fun AuthMethodSelector(
  selectedMethod: JellyseerrAuthMethod?,
  onMethodSelected: (JellyseerrAuthMethod) -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.medium,
    colors = CardDefaults.cardColors(),
  ) {
    FlowRow(
      modifier = Modifier.fillMaxWidth(),
    ) {
      JellyseerrAuthMethod.entries.forEach { method ->
        AuthMethodButton(
          method = method,
          isSelected = method == selectedMethod,
          onClick = { onMethodSelected(method) },
          modifier = Modifier.weight(1f),
        )
      }
    }
  }
}

@Composable
private fun AuthMethodButton(
  method: JellyseerrAuthMethod,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val backgroundColor by animateColorAsState(
    targetValue = if (isSelected) {
      Color(0xFF6366F1)
    } else {
      Color.Transparent
    },
    animationSpec = tween(300),
    label = "",
  )

  val textColor by animateColorAsState(
    targetValue = if (isSelected) {
      Color.White
    } else {
      MaterialTheme.colorScheme.onSurfaceVariant
    },
    animationSpec = tween(300),
    label = "",
  )

  val scale by animateFloatAsState(
    targetValue = if (isSelected) 1.02f else 1f,
    animationSpec = tween(300),
    label = "",
  )

  Box(
    modifier = modifier
      .scale(scale)
      .clip(MaterialTheme.shapes.medium)
      .background(backgroundColor)
      .clickable { onClick() }
      .padding(
        vertical = MaterialTheme.dimensions.keyline_12,
        horizontal = MaterialTheme.dimensions.keyline_8,
      ),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = method.displayName,
      color = textColor,
      style = MaterialTheme.typography.labelLarge,
      textAlign = TextAlign.Center,
    )
  }
}

@Composable
private fun LoginForm(
  state: JellyseerrState.Login,
  interaction: (JellyseerrInteraction) -> Unit,
) {
  val label = remember(state.loginData.authMethod) {
    if (state.loginData.authMethod == JellyseerrAuthMethod.JELLYSEERR) {
      Res.string.feature_settings_email
    } else {
      Res.string.feature_settings_username
    }
  }

  Column {
    OutlinedTextField(
      value = state.loginData.username.value,
      onValueChange = {
        interaction.invoke(JellyseerrInteraction.OnUsernameChange(it))
      },
      label = { Text(stringResource(label)) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF6366F1),
        focusedLabelColor = Color(0xFF6366F1),
        cursorColor = Color(0xFF6366F1),
      ),
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
      ),
      singleLine = true,
    )

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))

    PasswordOutlinedTextField(
      modifier = Modifier
        .semantics {
          contentType = ContentType.Password
        }
        .testTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD)
        .fillMaxWidth(),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF6366F1),
        focusedLabelColor = Color(0xFF6366F1),
        cursorColor = Color(0xFF6366F1),
      ),
      value = state.loginData.password.value,
      onValueChange = { interaction.invoke(JellyseerrInteraction.OnPasswordChange(it)) },
    )
  }
}

@Composable
private fun LoginButton(
  selectedMethod: JellyseerrAuthMethod,
  isLoading: Boolean,
  enabled: Boolean,
  onClick: () -> Unit,
) {
  Button(
    onClick = onClick,
    modifier = Modifier
      .testTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON)
      .fillMaxWidth()
      .height(MaterialTheme.dimensions.keyline_56),
    enabled = enabled && !isLoading,
    shape = MaterialTheme.shapes.medium,
    colors = ButtonDefaults.buttonColors(
      containerColor = Color(0xFF6366F1),
      disabledContainerColor = Color(0x4D6366F1),
    ),
  ) {
    if (isLoading) {
      CircularProgressIndicator(
        modifier = Modifier.size(MaterialTheme.dimensions.keyline_20),
        color = Color.White,
        strokeWidth = MaterialTheme.dimensions.keyline_2,
      )
      Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_8))
      Text(
        text = stringResource(Res.string.feature_settings_signing_in),
        color = if (enabled) {
          Color.White
        } else {
          MaterialTheme.colorScheme.onSurfaceVariant
        },
      )
    } else {
      Text(
        text = stringResource(Res.string.feature_settings_sign_in_with, selectedMethod.displayName),
        color = if (enabled) {
          Color.White
        } else {
          MaterialTheme.colorScheme.onSurfaceVariant
        },
      )
    }
  }
}

@Previews
@Composable
fun JellyseerrLoginContentPreview(
  @PreviewParameter(JellyseerrLoginStatePreviewParameterProvider::class) state:
  JellyseerrState.Login,
) {
  AppTheme {
    Surface {
      JellyseerrLoginContent(
        state = state,
        interaction = {},
      )
    }
  }
}
