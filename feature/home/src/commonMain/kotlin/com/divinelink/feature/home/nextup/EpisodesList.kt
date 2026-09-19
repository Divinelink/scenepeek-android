package com.divinelink.feature.home.nextup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.track.NextUpEpisode
import com.divinelink.core.model.track.NextUpSection
import com.divinelink.core.model.ui.ExpandableSection
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.composition.rememberExpandState
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.rememberIsLandscape
import com.divinelink.core.ui.resources.core_ui_collapse
import com.divinelink.core.ui.resources.core_ui_expand
import com.divinelink.core.ui.resources.on_hold
import com.divinelink.core.ui.resources.upcoming
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun EpisodesList(
  modifier: Modifier,
  section: NextUpSection,
  episodes: List<NextUpEpisode>,
  action: (NextUpAction) -> Unit,
  switchAction: (SwitchPreferencesAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val onHoldExpanded = rememberExpandState(ExpandableSection.ON_HOLD_SHOWS)
  val onHoldRotationState by animateFloatAsState(
    targetValue = if (onHoldExpanded) -90f else 0f,
    animationSpec = tween(durationMillis = 300),
    label = "IconRotationAnimation",
  )
  val listState = rememberLazyListState()

  when (section) {
    NextUpSection.Available -> Unit
    NextUpSection.Upcoming -> Text(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
      text = stringResource(UiString.upcoming),
      style = MaterialTheme.typography.titleMedium,
    )
    NextUpSection.Paused -> TextButton(
      onClick = {
        switchAction(SwitchPreferencesAction.UpdateExpandedState(ExpandableSection.ON_HOLD_SHOWS))
      },
    ) {
      Text(
        text = stringResource(UiString.on_hold),
        style = MaterialTheme.typography.titleMedium,
      )
      Icon(
        modifier = Modifier.rotate(onHoldRotationState),
        imageVector = Icons.Filled.ArrowDropDown,
        contentDescription = if (onHoldExpanded) {
          stringResource(UiString.core_ui_collapse)
        } else {
          stringResource(UiString.core_ui_expand)
        },
      )
    }
  }

  AnimatedVisibility(
    visible = when (section) {
      NextUpSection.Available -> true
      NextUpSection.Upcoming -> true
      NextUpSection.Paused -> onHoldExpanded
    },
    enter = fadeIn() + slideInHorizontally(),
    exit = fadeOut() + slideOutHorizontally(),
  ) {
    LazyRow(
      modifier = modifier.fillMaxWidth(),
      state = listState,
      contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_4),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      items(
        items = episodes,
        key = { it.episode.showId },
      ) { nextUp ->
        NextUpEpisode(
          modifier = Modifier.conditional(
            condition = rememberIsLandscape(),
            ifTrue = { fillParentMaxWidth(0.5f) },
            ifFalse = { fillParentMaxWidth(0.85f) },
          ),
          nextUp = nextUp,
          section = section,
          action = action,
          onNavigate = onNavigate,
        )
      }
    }
  }
}
