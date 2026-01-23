package com.divinelink.feature.season.ui.forms.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.commons.extensions.toLocalDate
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.season.SeasonData
import com.divinelink.core.model.details.season.SeasonInformation
import com.divinelink.core.ui.SimpleInformationRow
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.extension.localizeFull
import com.divinelink.core.ui.resources.core_ui_aired_episodes
import com.divinelink.core.ui.resources.core_ui_episodes
import com.divinelink.core.ui.resources.core_ui_first_air_date
import com.divinelink.core.ui.resources.core_ui_information
import com.divinelink.core.ui.resources.core_ui_last_air_date
import com.divinelink.core.ui.resources.core_ui_total_runtime
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutFormContent(
  modifier: Modifier = Modifier,
  aboutData: SeasonData.About,
) {
  ScenePeekLazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag(TestTags.Details.About.FORM),
    contentPadding = PaddingValues(
      top = MaterialTheme.dimensions.keyline_16,
      start = MaterialTheme.dimensions.keyline_16,
      end = MaterialTheme.dimensions.keyline_16,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {

    if (!aboutData.overview.isNullOrEmpty()) {
      item {
        Text(
          text = aboutData.overview!!,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
      item {
        HorizontalDivider()
      }
    }

    item {
      SeasonAboutInformation(aboutData.information)
    }

    item {
      Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
    }
  }
}

@Composable
fun SeasonAboutInformation(
  info: SeasonInformation,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = stringResource(UiString.core_ui_information),
      style = MaterialTheme.typography.titleMedium,
    )

    SimpleInformationRow(
      title = stringResource(UiString.core_ui_episodes),
      data = info.totalEpisodes.toString(),
    )

    info.airedEpisodes?.let { episodes ->
      SimpleInformationRow(
        title = stringResource(UiString.core_ui_aired_episodes),
        data = episodes.toString(),
      )
    }

    info.firstAirDate?.toLocalDate().localizeFull()?.let { airDate ->
      SimpleInformationRow(
        title = stringResource(UiString.core_ui_first_air_date),
        data = airDate,
      )
    }

    info.lastAirDate?.toLocalDate().localizeFull()?.let { airDate ->
      SimpleInformationRow(
        title = stringResource(UiString.core_ui_last_air_date),
        data = airDate,
      )
    }

    info.totalRuntime?.let { runtime ->
      SimpleInformationRow(
        title = stringResource(UiString.core_ui_total_runtime),
        data = runtime,
      )
    }
  }
}
