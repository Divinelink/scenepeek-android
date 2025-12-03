package com.divinelink.feature.discover.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_clear_all
import com.divinelink.feature.discover.resources.Res
import com.divinelink.feature.discover.resources.feature_discover_minimum_user_votes
import com.divinelink.feature.discover.resources.feature_discover_user_score
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
fun RatingFiltersContent(
  voteAverage: DiscoverFilter.VoteAverage,
  minimumVotes: DiscoverFilter.MinimumVotes,
  action: (SelectFilterAction) -> Unit,
  onDismissRequest: () -> Unit,
) {
  val density = LocalDensity.current

  Box {
    var actionsSize by remember { mutableStateOf(0.dp) }

    LazyColumn(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_16)
        .padding(
          bottom = actionsSize.plus(MaterialTheme.dimensions.keyline_8),
        ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      item {
        Text(
          text = stringResource(
            Res.string.feature_discover_user_score,
            voteAverage.greaterThan,
            voteAverage.lessThan,
          ),
          style = MaterialTheme.typography.titleMedium,
        )
      }

      item {
        RangeSlider(
          onValueChange = {
            action(
              SelectFilterAction.UpdateVoteRange(
                DiscoverFilter.VoteAverage(
                  greaterThan = it.start.roundToInt(),
                  lessThan = it.endInclusive.roundToInt(),
                ),
              ),
            )
          },
          valueRange = 0f..10f,
          value = voteAverage.greaterThan.toFloat()..voteAverage.lessThan.toFloat(),
          steps = 9,
        )
      }

      item {
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_24))
      }

      item {
        Text(
          text = stringResource(Res.string.feature_discover_minimum_user_votes, minimumVotes.votes),
          style = MaterialTheme.typography.titleMedium,
        )
      }

      item {
        Slider(
          onValueChange = { action(SelectFilterAction.UpdateMinimumVotes((it.roundToInt()))) },
          valueRange = 0f..500f,
          value = minimumVotes.votes.toFloat(),
          steps = 9,
        )
      }
    }

    Row(
      modifier = Modifier
        .onSizeChanged {
          with(density) {
            actionsSize = it.height.toDp()
          }
        }
        .padding(horizontal = MaterialTheme.dimensions.keyline_16)
        .align(Alignment.BottomCenter)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ElevatedButton(
        enabled = voteAverage.greaterThan != 0 ||
          voteAverage.lessThan != 10 ||
          minimumVotes.votes != 10,
        modifier = Modifier.weight(1f),
        onClick = {
          action(SelectFilterAction.ResetRatingFilters)
          onDismissRequest()
        },
      ) {
        Text(text = stringResource(UiString.core_ui_clear_all))
      }
    }
  }
}
