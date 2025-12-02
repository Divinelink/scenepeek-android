package com.divinelink.feature.request.media

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.canBeRequested
import com.divinelink.core.model.details.isAvailable
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.button.action.ActionButton
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.components.dialog.TwoButtonDialog
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.core_ui_advanced
import com.divinelink.core.ui.core_ui_cancel
import com.divinelink.core.ui.core_ui_episodes
import com.divinelink.core.ui.core_ui_request_movie
import com.divinelink.core.ui.core_ui_request_pending_request
import com.divinelink.core.ui.core_ui_request_series
import com.divinelink.core.ui.core_ui_season
import com.divinelink.core.ui.core_ui_season_number
import com.divinelink.core.ui.core_ui_status
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.request.media.components.DestinationServerDropDownMenu
import com.divinelink.feature.request.media.components.JellyseerrGradientText
import com.divinelink.feature.request.media.components.QualityProfileDropDownMenu
import com.divinelink.feature.request.media.components.RootFolderDropDownMenu
import com.divinelink.feature.request.media.provider.RequestMediaUiStateProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun RequestMediaContent(
  state: RequestMediaUiState,
  onDismissRequest: () -> Unit,
  onAction: (RequestMediaAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val density = LocalDensity.current
  val showCancelRequest by remember(state.selectedSeasons) {
    derivedStateOf { state.isEditMode && state.selectedSeasons.isEmpty() }
  }

  var actionsSize by remember { mutableStateOf(0.dp) }

  SnackbarMessageHandler(
    snackbarMessage = state.snackbarMessage,
    onDismissSnackbar = { onAction(RequestMediaAction.DismissSnackbar) },
    onShowMessage = onDismissRequest,
  )

  state.dialogState?.let { dialogState ->
    TwoButtonDialog(
      state = dialogState,
      onDismissRequest = { onAction(RequestMediaAction.DismissDialog) },
      onDismiss = { onAction(RequestMediaAction.DismissDialog) },
      onConfirm = {
        onAction(RequestMediaAction.DismissDialog)
        onNavigate(Navigation.JellyseerrSettingsRoute(withNavigationBar = true))
      },
    )
  }

  Box {
    AnimatedVisibility(state.isLoading) {
      LinearProgressIndicator(
        modifier = Modifier
          .align(Alignment.TopCenter)
          .testTag(TestTags.LINEAR_LOADING_INDICATOR)
          .fillMaxWidth(),
      )
    }
    LazyColumn(
      modifier = Modifier
        .testTag(TestTags.LAZY_COLUMN)
        .padding(
          top = MaterialTheme.dimensions.keyline_8,
          bottom = actionsSize.plus(MaterialTheme.dimensions.keyline_8),
        ),
    ) {
      item {
        JellyseerrGradientText(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          text = if (state.isEditMode) {
            stringResource(UiString.core_ui_request_pending_request)
          } else if (state.media.mediaType == MediaType.TV) {
            stringResource(UiString.core_ui_request_series)
          } else {
            stringResource(UiString.core_ui_request_movie)
          },
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_4))
        Text(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          text = state.media.name,
          style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_8))
      }

      if (state.media.mediaType == MediaType.TV) {
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(color = MaterialTheme.colorScheme.surfaceContainer),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          ) {
            Switch(
              checked = state.selectedSeasons.size == state.requestableSeasons.size,
              enabled = state.requestableSeasons.isNotEmpty(),
              onCheckedChange = {
                onAction(RequestMediaAction.SelectAllSeasons(selectAll = it))
              },
              modifier = Modifier
                .testTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH)
                .weight(0.6f),
            )

            Text(
              text = stringResource(UiString.core_ui_season),
              modifier = Modifier
                .weight(1f),
              style = MaterialTheme.typography.titleSmall,
            )

            Text(
              text = stringResource(UiString.core_ui_episodes),
              modifier = Modifier
                .weight(1f),
              style = MaterialTheme.typography.titleSmall,
            )

            Text(
              text = stringResource(UiString.core_ui_status),
              modifier = Modifier
                .weight(1f),
              style = MaterialTheme.typography.titleSmall,
            )
          }
        }

        items(
          items = state.validSeasons,
          key = { it.seasonNumber },
        ) { item ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .testTag(TestTags.Dialogs.SEASON_ROW.format(item.seasonNumber))
              .clickable(
                enabled = item.seasonNumber in state.requestableSeasons || item.canBeRequested(),
              ) {
                onAction(RequestMediaAction.SelectSeason(item.seasonNumber))
              },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          ) {
            Switch(
              checked = item.seasonNumber in state.selectedSeasons ||
                (item.seasonNumber !in state.requestableSeasons && item.isAvailable()),
              enabled = item.seasonNumber in state.requestableSeasons || item.canBeRequested(),
              onCheckedChange = {
                onAction(RequestMediaAction.SelectSeason(item.seasonNumber))
              },
              modifier = Modifier
                .testTag(TestTags.Dialogs.SEASON_SWITCH.format(item.seasonNumber))
                .weight(0.6f),
            )

            Text(
              text = stringResource(UiString.core_ui_season_number, item.seasonNumber),
              overflow = TextOverflow.MiddleEllipsis,
              maxLines = 1,
              modifier = Modifier
                .weight(1f),
              style = MaterialTheme.typography.bodyMedium,
            )

            Text(
              text = item.episodeCount.toString(),
              modifier = Modifier
                .weight(1f),
              style = MaterialTheme.typography.bodyMedium,
            )

            Box(
              modifier = Modifier.weight(1f),
            ) {
              item.status?.let {
                JellyseerrStatusPill(status = it)
              }
            }
          }

          HorizontalDivider()
        }
      }

      item {
        if (
          state.selectedInstance !is LCEState.Idle ||
          state.selectedProfile !is LCEState.Idle ||
          state.selectedRootFolder !is LCEState.Idle
        ) {
          Text(
            modifier = Modifier
              .padding(horizontal = MaterialTheme.dimensions.keyline_16)
              .padding(
                top = MaterialTheme.dimensions.keyline_8,
                bottom = MaterialTheme.dimensions.keyline_4,
              ),
            text = stringResource(UiString.core_ui_advanced),
            style = MaterialTheme.typography.titleMedium,
          )
        }
      }

      item {
        AnimatedVisibility(state.instances.size > 1 || state.selectedInstance == LCEState.Loading) {
          DestinationServerDropDownMenu(
            modifier = Modifier
              .testTag(
                TestTags.Request.DESTINATION_SERVER_MENU.format(
                  state.selectedInstance::class.simpleName.toString(),
                ),
              )
              .padding(
                horizontal = MaterialTheme.dimensions.keyline_16,
                vertical = MaterialTheme.dimensions.keyline_4,
              ),
            enabled = !state.isLoading,
            options = state.instances,
            currentInstance = state.selectedInstance,
            onUpdate = { onAction(RequestMediaAction.SelectInstance(it)) },
          )
        }
      }

      item {
        AnimatedVisibility(
          visible = state.profiles.size > 1 || state.selectedProfile == LCEState.Loading,
        ) {
          QualityProfileDropDownMenu(
            modifier = Modifier
              .fillMaxWidth()
              .testTag(
                TestTags.Request.QUALITY_PROFILE_MENU.format(
                  state.selectedProfile::class.simpleName.toString(),
                ),
              )
              .padding(
                horizontal = MaterialTheme.dimensions.keyline_16,
                vertical = MaterialTheme.dimensions.keyline_4,
              ),
            enabled = !state.isLoading,
            options = state.profiles,
            currentInstance = state.selectedProfile,
            onUpdate = { onAction(RequestMediaAction.SelectQualityProfile(it)) },
          )
        }
      }

      item {
        AnimatedVisibility(
          state.rootFolders.size > 1 || state.selectedRootFolder == LCEState.Loading,
        ) {
          RootFolderDropDownMenu(
            modifier = Modifier
              .testTag(
                TestTags.Request.ROOT_FOLDER_MENU.format(
                  state.selectedRootFolder::class.simpleName.toString(),
                ),
              )
              .padding(
                horizontal = MaterialTheme.dimensions.keyline_16,
                vertical = MaterialTheme.dimensions.keyline_4,
              ),
            enabled = !state.isLoading,
            options = state.rootFolders,
            currentInstance = state.selectedRootFolder,
            onUpdate = { onAction(RequestMediaAction.SelectRootFolder(it)) },
          )
        }
      }
    }

    Column(
      modifier = Modifier
        .onSizeChanged {
          with(density) {
            actionsSize = it.height.toDp()
          }
        }
        .align(Alignment.BottomCenter)
        .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    ) {
      ElevatedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onDismissRequest() },
      ) {
        Text(text = stringResource(UiString.core_ui_cancel))
      }
      if (state.media.mediaType == MediaType.TV) {
        AnimatedContent(targetState = showCancelRequest) { showCancel ->
          if (showCancel) {
            ActionButton.CancelRequest(
              enabled = state.isReady,
              onClick = { onAction(RequestMediaAction.CancelRequest) },
            )
          } else if (state.isEditMode) {
            ActionButton.EditRequest(
              modifier = Modifier.weight(1f),
              enabled = state.isReady,
              onClick = { onAction(RequestMediaAction.RequestMedia) },
            )
          } else {
            ActionButton.RequestTVShow(
              enabled = state.isReady,
              selectedSeasons = state.selectedSeasons.size,
              onClick = { onAction(RequestMediaAction.RequestMedia) },
            )
          }
        }
      } else {
        if (state.isEditMode) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            ActionButton.CancelRequest(
              modifier = Modifier.weight(1f),
              enabled = state.isReady,
              onClick = { onAction(RequestMediaAction.CancelRequest) },
            )
            ActionButton.EditRequest(
              modifier = Modifier.weight(1f),
              enabled = state.isReady,
              onClick = { onAction(RequestMediaAction.RequestMedia) },
            )
          }
        } else {
          ActionButton.RequestMovie(
            enabled = state.isReady,
            onClick = { onAction(RequestMediaAction.RequestMedia) },
          )
        }
      }
    }
  }
}

@Composable
@Previews
fun RequestMediaContentPreview(
  @PreviewParameter(RequestMediaUiStateProvider::class) uiState: RequestMediaUiState,
) {
  PreviewLocalProvider {
    RequestMediaContent(
      state = uiState,
      onDismissRequest = {},
      onNavigate = {},
      onAction = {},
    )
  }
}
