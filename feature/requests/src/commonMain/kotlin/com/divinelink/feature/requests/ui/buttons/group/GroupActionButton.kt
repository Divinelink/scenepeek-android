package com.divinelink.feature.requests.ui.buttons.group

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ButtonGroupDefaults.connectedMiddleButtonPressShape
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.model.jellyseerr.media.SeerrRequestAction
import com.divinelink.feature.requests.ui.SeerrActionDefaults
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SeerrRequestActionsButtonGroup(
  modifier: Modifier = Modifier,
  actions: List<SeerrRequestAction>,
  enabled: Boolean,
  action: (SeerrRequestAction) -> Unit,
) {
  val interactionSources = remember { List(actions.size) { MutableInteractionSource() } }
  key(actions.size, actions.hashCode()) {
    ButtonGroup(
      modifier = modifier,
      expandedRatio = 0.07f,
      overflowIndicator = { menuState ->
        ButtonGroupDefaults.OverflowIndicator(menuState = menuState)
      },
    ) {
      actions.forEachIndexed { index, requestAction ->
        customItem(
          buttonGroupContent = {
            val contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            val color = SeerrActionDefaults.getColor(requestAction)
            val label = SeerrActionDefaults.getLabel(requestAction)

            val textColor by animateColorAsState(
              targetValue = when (enabled) {
                true -> Color.White
                false -> Color.White.copy(alpha = 0.6f)
              },
              label = "TextColorAnimation",
            )

            val buttonColor by animateColorAsState(
              targetValue = when (enabled) {
                true -> color
                false -> color.copy(alpha = 0.6f)
              },
              label = "ButtonColorAnimation",
            )

            Button(
              enabled = enabled,
              colors = ButtonDefaults.buttonColors().copy(
                containerColor = buttonColor,
              ),
              shapes = ButtonShapes(
                shape = ShapeDefaults.ExtraExtraLarge,
                pressedShape = connectedMiddleButtonPressShape,
              ),
              contentPadding = contentPadding,
              interactionSource = interactionSources[index],
              modifier = Modifier.animateWidth(
                interactionSource = interactionSources[index],
              ),
              onClick = { action(requestAction) },
            ) {
              Icon(
                imageVector = requestAction.icon,
                tint = textColor,
                contentDescription = null,
              )
              Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
              Text(
                text = stringResource(label),
                softWrap = false,
                maxLines = 1,
                overflow = TextOverflow.Visible,
                color = textColor,
              )
            }
          },
          menuContent = { menuState ->
            val color = SeerrActionDefaults.getColor(requestAction)
            val label = SeerrActionDefaults.getLabel(requestAction)

            DropdownMenuItem(
              leadingIcon = {
                Icon(
                  imageVector = requestAction.icon,
                  tint = color,
                  contentDescription = null,
                )
              },
              text = {
                Text(
                  text = stringResource(label),
                  color = color,
                )
              },
              onClick = {
                menuState.dismiss()
                action(requestAction)
              },
              interactionSource = interactionSources[index],
            )
          },
        )
      }
    }
  }
}
