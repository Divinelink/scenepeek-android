package com.divinelink.feature.home.nextup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.track.NextUpEpisode
import com.divinelink.core.model.track.NextUpSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.rememberIsLandscape
import com.divinelink.core.ui.resources.upcoming
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun EpisodesList(
  modifier: Modifier,
  section: NextUpSection,
  episodes: List<NextUpEpisode>,
  action: (NextUpAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  when (section) {
    NextUpSection.Available -> Unit
    NextUpSection.Upcoming -> Text(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
      text = stringResource(UiString.upcoming),
      style = MaterialTheme.typography.titleMedium,
    )
  }

  LazyRow(
    modifier = modifier.fillMaxWidth(),
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
