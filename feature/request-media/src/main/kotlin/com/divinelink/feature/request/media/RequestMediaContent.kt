package com.divinelink.feature.request.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.canBeRequested
import com.divinelink.core.model.details.isAvailable
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.components.dialog.TwoButtonDialog
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.request.media.components.DestinationServerDropDownMenu
import com.divinelink.feature.request.media.components.JellyseerrGradientText
import com.divinelink.feature.request.media.components.QualityProfileDropDownMenu
import com.divinelink.feature.request.media.components.RootFolderDropDownMenu
import com.divinelink.feature.request.media.provider.RequestMediaUiStateProvider

@Composable
fun RequestMediaContent(
  state: RequestMediaUiState,
  onDismissRequest: () -> Unit,
  onAction: (RequestMediaAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val selectedSeasons = remember { mutableStateListOf<Int>() }
  val validSeasons = state.seasons.filterNot { it.seasonNumber == 0 }

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
          bottom = MaterialTheme.dimensions.keyline_96,
        ),
      contentPadding = PaddingValues(bottom = MaterialTheme.dimensions.keyline_16),
    ) {
      item {
        JellyseerrGradientText(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          text = if (state.isEditMode) {
            stringResource(id = UiString.core_ui_request_pending_request)
          } else if (state.media.mediaType == MediaType.TV) {
            stringResource(id = UiString.core_ui_request_series)
          } else {
            stringResource(id = UiString.core_ui_request_movie)
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
              checked = selectedSeasons.size == validSeasons.filter { it.canBeRequested() }.size,
              enabled = validSeasons.any { it.canBeRequested() },
              onCheckedChange = {
                if (it) {
                  selectedSeasons.clear()
                  selectedSeasons.addAll(
                    validSeasons.mapIndexedNotNull { index, season ->
                      if (!season.isAvailable()) index + 1 else null
                    },
                  )
                } else {
                  selectedSeasons.clear()
                }
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
          items = validSeasons,
          key = { it.seasonNumber },
        ) { item ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .testTag(TestTags.Dialogs.SEASON_ROW.format(item.seasonNumber))
              .clickable(enabled = item.canBeRequested()) {
                if (item.canBeRequested()) {
                  if (selectedSeasons.contains(item.seasonNumber)) {
                    selectedSeasons.remove(item.seasonNumber)
                  } else {
                    selectedSeasons.add(item.seasonNumber)
                  }
                }
              },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          ) {
            Switch(
              checked = selectedSeasons.contains(item.seasonNumber) || item.isAvailable(),
              enabled = item.canBeRequested(),
              onCheckedChange = {
                if (it) {
                  selectedSeasons.add(item.seasonNumber)
                } else {
                  selectedSeasons.remove(item.seasonNumber)
                }
              },
              modifier = Modifier
                .testTag(TestTags.Dialogs.SEASON_SWITCH.format(item.seasonNumber))
                .weight(0.6f),
            )

            Text(
              text = stringResource(id = UiString.core_ui_season_number, item.seasonNumber),
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
                  state.selectedInstance::class.simpleName,
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
                  state.selectedProfile::class.simpleName,
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
                  state.selectedRootFolder::class.simpleName,
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
      modifier = Modifier.align(Alignment.BottomCenter),
    ) {
      ElevatedButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.dimensions.keyline_16),
        onClick = { onDismissRequest() },
      ) {
        Text(text = stringResource(id = UiString.core_ui_cancel))
      }
      if (state.media.mediaType == MediaType.TV) {
        RequestSeasonsButton(selectedSeasons, state, onAction)
      } else {
        RequestMovieButton(
          isEditMode = state.isEditMode,
          onAction = onAction,
        )
      }
    }
  }
}

@Composable
private fun RequestMovieButton(
  isEditMode: Boolean,
  onAction: (RequestMediaAction) -> Unit,
) {
  Button(
    modifier = Modifier
      .fillMaxWidth()
      .testTag(TestTags.Dialogs.REQUEST_MOVIE_BUTTON)
      .padding(bottom = MaterialTheme.dimensions.keyline_8)
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    onClick = {
      onAction(RequestMediaAction.RequestMedia(emptyList()))
    },
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(Icons.Default.Download, null)
      Text(
        text = if (isEditMode) {
          stringResource(id = UiString.core_ui_edit_request)
        } else {
          stringResource(id = UiString.core_ui_request)
        },
      )
    }
  }
}

@Composable
private fun RequestSeasonsButton(
  selectedSeasons: SnapshotStateList<Int>,
  state: RequestMediaUiState,
  onAction: (RequestMediaAction) -> Unit,
) {
  Button(
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = MaterialTheme.dimensions.keyline_8)
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    enabled = selectedSeasons.isNotEmpty() && !state.isLoading,
    onClick = { onAction(RequestMediaAction.RequestMedia(selectedSeasons)) },
  ) {
    val text = if (state.isEditMode) {
      stringResource(id = UiString.core_ui_edit_request)
    } else if (selectedSeasons.isEmpty()) {
      stringResource(id = UiString.core_ui_select_seasons_button)
    } else {
      pluralStringResource(
        id = UiPlurals.core_ui_request_series_button,
        count = selectedSeasons.size,
        selectedSeasons.size,
      )
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      AnimatedVisibility(selectedSeasons.isNotEmpty()) {
        Icon(Icons.Default.Download, null)
      }
      Text(text = text)
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
