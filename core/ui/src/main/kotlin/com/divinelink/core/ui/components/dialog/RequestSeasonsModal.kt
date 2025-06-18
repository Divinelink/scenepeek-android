package com.divinelink.core.ui.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.canBeRequested
import com.divinelink.core.model.details.isAvailable
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.JellyseerrStatusPill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestSeasonsModal(
  seasons: List<Season>,
  onRequestClick: (List<Int>) -> Unit,
  onDismissRequest: () -> Unit,
) {
  val selectedSeasons = remember { mutableStateListOf<Int>() }
  val validSeasons = seasons.filterNot { it.seasonNumber == 0 }

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    modifier = Modifier.testTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      Box {
        LazyColumn(
          modifier = Modifier
            .padding(vertical = MaterialTheme.dimensions.keyline_24)
            .padding(bottom = MaterialTheme.dimensions.keyline_96),
        ) {
          item {
            Text(
              modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
              text = stringResource(id = R.string.core_ui_request_series),
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
                        if (!season.isAvailable()) index else null
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
                text = stringResource(R.string.core_ui_season),
                modifier = Modifier
                  .weight(1f),
                style = MaterialTheme.typography.titleSmall,
              )

              Text(
                text = stringResource(R.string.core_ui_episodes),
                modifier = Modifier
                  .weight(1f),
                style = MaterialTheme.typography.titleSmall,
              )

              Text(
                text = stringResource(R.string.core_ui_status),
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
                text = stringResource(id = R.string.core_ui_season_number, item.seasonNumber),
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
            Text(text = stringResource(id = R.string.core_ui_cancel))
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
              stringResource(id = R.string.core_ui_select_seasons_button)
            } else {
              pluralStringResource(
                id = R.plurals.core_ui_request_series_button,
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
    },
  )
}

@Previews
@Composable
private fun SelectSeasonsDialogPreview() {
  AppTheme {
    Surface {
      RequestSeasonsModal(
        seasons = SeasonFactory.allWithStatus(),
        onRequestClick = {},
        onDismissRequest = {},
      )
    }
  }
}
