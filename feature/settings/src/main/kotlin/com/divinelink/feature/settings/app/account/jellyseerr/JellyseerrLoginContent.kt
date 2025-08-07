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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.PasswordOutlinedTextField
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R

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
        text = stringResource(R.string.feature_settings_jellyseerr_description),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
      )
    }

    item {
      OutlinedTextField(
        modifier = Modifier
          .testTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
          .fillMaxWidth(),
        value = state.loginData.address,
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
        label = { Text(text = stringResource(R.string.feature_settings_jellyseerr_address)) },
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
      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_96))
    }
  }
}

@Previews
@Composable
private fun JellyseerrBottomSheetContentPreview() {
  AppTheme {
    Surface {
      JellyseerrLoginContent(
        state = JellyseerrState.Login(
          isLoading = false,
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
      JellyseerrLoginContent(
        state = JellyseerrState.Login(
          isLoading = true,
        ),
        interaction = {},
      )
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
      fontWeight = FontWeight.Medium,
      fontSize = 14.sp,
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
      R.string.feature_settings_email
    } else {
      R.string.feature_settings_username
    }
  }

  Column {
    OutlinedTextField(
      value = state.loginData.username.value,
      onValueChange = {
        interaction.invoke(JellyseerrInteraction.OnUsernameChange(it))
      },
      label = { Text(stringResource(label)) },
      modifier = Modifier.fillMaxWidth(),
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
        .testTag(TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD)
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
      .fillMaxWidth()
      .height(52.dp),
    enabled = enabled && !isLoading,
    shape = RoundedCornerShape(8.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = Color(0xFF6366F1),
      disabledContainerColor = Color(0x4D6366F1),
    ),
  ) {
    if (isLoading) {
      CircularProgressIndicator(
        modifier = Modifier.size(20.dp),
        color = Color.White,
        strokeWidth = 2.dp,
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = stringResource(R.string.feature_settings_signing_in),
        color = Color.White,
      )
    } else {
      Text(
        text = stringResource(R.string.feature_settings_sign_in_with, selectedMethod.displayName),
        color = Color.White,
      )
    }
  }
}
