package com.divinelink.feature.request.media
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.canBeRequested
import com.divinelink.core.model.details.isAvailable
import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstance
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.JellyseerrStatusPill
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestSeasonsModal(
  seasons: List<Season>,
  viewModel: RequestSeasonsViewModel = koinViewModel(),
  onRequestClick: (List<Int>) -> Unit,
  onDismissRequest: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(seasons) {
    viewModel.updateSeasons(seasons)
  }

  ModalBottomSheet(
    modifier = Modifier.testTag(TestTags.Modal.REQUEST_SEASONS),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      RequestSeasonsContent(
        state = uiState,
        onDismissRequest = onDismissRequest,
        onRequestClick = onRequestClick,
        onUpdateInstance = viewModel::selectInstance,
        onUpdateRootFolder = viewModel::selectRootFolder,
        onUpdateQualityProfile = viewModel::selectQualityProfile,
      )
    },
  )
}

@Composable
private fun RequestSeasonsContent(
  state: RequestSeasonsUiState,
  onDismissRequest: () -> Unit,
  onRequestClick: (List<Int>) -> Unit,
  onUpdateInstance: (SonarrInstance) -> Unit,
  onUpdateQualityProfile: (InstanceProfile) -> Unit,
  onUpdateRootFolder: (InstanceRootFolder) -> Unit,
) {
  val selectedSeasons = remember { mutableStateListOf<Int>() }
  val validSeasons = state.seasons.filterNot { it.seasonNumber == 0 }

  Box {
    LazyColumn(
      modifier = Modifier
        .padding(vertical = MaterialTheme.dimensions.keyline_24)
        .padding(bottom = MaterialTheme.dimensions.keyline_96),
    ) {
      item {
        Text(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
          text = stringResource(id = UiString.core_ui_request_series),
          style = MaterialTheme.typography.headlineSmall,
        )
      }
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          Switch(
            checked = selectedSeasons.size == validSeasons
              .filter { it.canBeRequested() }.size,
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
            modifier = Modifier
              .weight(1f),
          ) {
            item.status?.let {
              JellyseerrStatusPill(status = it)
            }
          }
        }

        HorizontalDivider()
      }

      item {
        Text(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
          text = stringResource(UiString.core_ui_advanced),
          style = MaterialTheme.typography.headlineSmall,
        )
      }

      item {
        AnimatedVisibility(state.instances.size > 1) {
          DestinationServerDropDownMenu(
            modifier = Modifier.padding(
              horizontal = MaterialTheme.dimensions.keyline_16,
              vertical = MaterialTheme.dimensions.keyline_4,
            ),
            options = state.instances,
            currentInstance = state.selectedInstance,
            onUpdate = onUpdateInstance,
          )
        }
      }

      item {
        QualityProfileDropDownMenu(
          modifier = Modifier.padding(
            horizontal = MaterialTheme.dimensions.keyline_16,
            vertical = MaterialTheme.dimensions.keyline_4,
          ),
          options = state.profiles,
          currentInstance = state.selectedProfile,
          onUpdate = onUpdateQualityProfile,
        )
      }

      item {
        RootFolderDropDownMenu(
          modifier = Modifier.padding(
            horizontal = MaterialTheme.dimensions.keyline_16,
            vertical = MaterialTheme.dimensions.keyline_4,
          ),
          options = state.rootFolders,
          currentInstance = state.selectedRootFolder,
          onUpdate = onUpdateRootFolder,
        )
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
      Button(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = MaterialTheme.dimensions.keyline_8)
          .padding(horizontal = MaterialTheme.dimensions.keyline_16),
        enabled = selectedSeasons.isNotEmpty(),
        onClick = {
          onRequestClick(selectedSeasons)
          onDismissRequest()
        },
      ) {
        val text = if (selectedSeasons.isEmpty()) {
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
  }
}

@Previews
@Composable
private fun SelectSeasonsDialogPreview() {
  AppTheme {
    Surface {
//      RequestSeasonsModal(
// //        seasons = SeasonFactory.allWithStatus(),
//        onRequestClick = {},
//        onDismissRequest = {},
//      )
    }
  }
}

@Previews
@Composable
fun RequestSeasonsContentPreview() {
  AppTheme {
    Surface {
//      RequestSeasonsContent(
//        seasons = SeasonFactory.allWithStatus(),
//        onRequestClick = {},
//        onDismissRequest = {},
//      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationServerDropDownMenu(
  modifier: Modifier = Modifier,
  options: List<SonarrInstance>,
  currentInstance: LCEState<SonarrInstance>,
  onUpdate: (SonarrInstance) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }

  val rotationState by animateFloatAsState(
    targetValue = if (expanded) 180f else 0f,
    animationSpec = tween(durationMillis = 300),
    label = "IconRotationAnimation",
  )

  ExposedDropdownMenuBox(
    modifier = modifier.animateContentSize(),
    expanded = expanded,
    onExpandedChange = { expanded = !expanded },
  ) {
    Crossfade(currentInstance) { state ->
      when (state) {
        is LCEState.Content -> OutlinedTextField(
          modifier = Modifier
            .fillMaxWidth()
            .menuAnchor(MenuAnchorType.PrimaryEditable),
          readOnly = true,
          value = state.data.name,
          onValueChange = {},
          label = { Text(stringResource(R.string.feature_request_media_destination_server)) },
          trailingIcon = {
            Icon(
              modifier = Modifier.rotate(rotationState),
              imageVector = Icons.Filled.ArrowDropUp,
              contentDescription = if (expanded) {
                stringResource(UiString.core_ui_collapse)
              } else {
                stringResource(UiString.core_ui_expand)
              },
            )
          },
        )
        LCEState.Error -> {
          // Do nothing
        }
        LCEState.Loading -> OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          readOnly = true,
          enabled = false,
          value = stringResource(UiString.core_ui_loading),
          onValueChange = {},
          label = { Text(stringResource(R.string.feature_request_media_destination_server)) },
        )
      }
    }

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      options.forEach { selectionOption ->
        DropdownMenuItem(
          text = {
            Text(
              text = selectionOption.name,
              style = MaterialTheme.typography.bodyMedium,
            )
          },
          onClick = {
            onUpdate(selectionOption)
            expanded = false
          },
          contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimensions.keyline_16,
            vertical = MaterialTheme.dimensions.keyline_4,
          ),
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QualityProfileDropDownMenu(
  modifier: Modifier = Modifier,
  options: List<InstanceProfile>,
  currentInstance: LCEState<InstanceProfile>,
  onUpdate: (InstanceProfile) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }

  val rotationState by animateFloatAsState(
    targetValue = if (expanded) 180f else 0f,
    animationSpec = tween(durationMillis = 300),
    label = "IconRotationAnimation",
  )

  ExposedDropdownMenuBox(
    modifier = modifier.animateContentSize(),
    expanded = expanded,
    onExpandedChange = { expanded = !expanded },
  ) {
    Crossfade(currentInstance) { state ->
      when (state) {
        is LCEState.Content -> OutlinedTextField(
          modifier = Modifier
            .fillMaxWidth()
            .menuAnchor(MenuAnchorType.PrimaryEditable),
          readOnly = true,
          value = buildString {
            append(state.data.name)
            if (state.data.isDefault) {
              append(
                " (${stringResource(R.string.feature_request_media_default)})",
              )
            }
          },
          onValueChange = {},
          label = { Text(stringResource(R.string.feature_request_media_quality_profile)) },
          trailingIcon = {
            Icon(
              modifier = Modifier.rotate(rotationState),
              imageVector = Icons.Filled.ArrowDropUp,
              contentDescription = if (expanded) {
                stringResource(UiString.core_ui_collapse)
              } else {
                stringResource(UiString.core_ui_expand)
              },
            )
          },
        )
        LCEState.Error -> {
          // Do nothing
        }
        LCEState.Loading -> OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          readOnly = true,
          enabled = false,
          value = stringResource(UiString.core_ui_loading),
          onValueChange = {},
          label = { Text(stringResource(R.string.feature_request_media_quality_profile)) },
        )
      }
    }

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      options.forEach { selectionOption ->
        DropdownMenuItem(
          text = {
            Text(
              text = buildString {
                append(selectionOption.name)
                if (selectionOption.isDefault) {
                  append(
                    " (${stringResource(R.string.feature_request_media_default)})",
                  )
                }
              },
              style = MaterialTheme.typography.bodyMedium,
            )
          },
          onClick = {
            onUpdate(selectionOption)
            expanded = false
          },
          contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimensions.keyline_16,
            vertical = MaterialTheme.dimensions.keyline_4,
          ),
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootFolderDropDownMenu(
  modifier: Modifier = Modifier,
  options: List<InstanceRootFolder>,
  currentInstance: LCEState<InstanceRootFolder>,
  onUpdate: (InstanceRootFolder) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }

  val rotationState by animateFloatAsState(
    targetValue = if (expanded) 180f else 0f,
    animationSpec = tween(durationMillis = 300),
    label = "IconRotationAnimation",
  )

  ExposedDropdownMenuBox(
    modifier = modifier.animateContentSize(),
    expanded = expanded,
    onExpandedChange = { expanded = !expanded },
  ) {
    Crossfade(currentInstance) { state ->
      when (state) {
        is LCEState.Content -> OutlinedTextField(
          modifier = Modifier
            .fillMaxWidth()
            .menuAnchor(MenuAnchorType.PrimaryEditable),
          readOnly = true,
          value = buildString {
            append(state.data.path)
            append(" (${state.data.freeSpace})")
            if (state.data.isDefault) {
              append(
                " (${stringResource(R.string.feature_request_media_default)})",
              )
            }
          },
          onValueChange = {},
          label = { Text(stringResource(R.string.feature_request_media_root_folder)) },
          trailingIcon = {
            Icon(
              modifier = Modifier.rotate(rotationState),
              imageVector = Icons.Filled.ArrowDropUp,
              contentDescription = if (expanded) {
                stringResource(UiString.core_ui_collapse)
              } else {
                stringResource(UiString.core_ui_expand)
              },
            )
          },
        )
        LCEState.Error -> {
          // Do nothing
        }
        LCEState.Loading -> OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          readOnly = true,
          enabled = false,
          value = stringResource(UiString.core_ui_loading),
          onValueChange = {},
          label = { Text(stringResource(R.string.feature_request_media_root_folder)) },
        )
      }
    }

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      options.forEach { selectionOption ->
        DropdownMenuItem(
          text = {
            Text(
              text = buildString {
                append(selectionOption.path)
                append(" (${selectionOption.freeSpace})")
                if (selectionOption.isDefault) {
                  append(
                    " (${stringResource(R.string.feature_request_media_default)})",
                  )
                }
              },
              style = MaterialTheme.typography.bodyMedium,
            )
          },
          onClick = {
            onUpdate(selectionOption)
            expanded = false
          },
          contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimensions.keyline_16,
            vertical = MaterialTheme.dimensions.keyline_4,
          ),
        )
      }
    }
  }
}
