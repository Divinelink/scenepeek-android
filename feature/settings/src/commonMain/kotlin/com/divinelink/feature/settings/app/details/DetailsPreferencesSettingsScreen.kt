package com.divinelink.feature.settings.app.details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.model.details.rating.MediaRatingSource
import com.divinelink.core.model.locale.Country
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.components.SettingsRadioPrefItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsSwitchItem
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_details_preferences
import com.divinelink.feature.settings.resources.feature_settings_movie_rating_preference
import com.divinelink.feature.settings.resources.feature_settings_ratings
import com.divinelink.feature.settings.resources.feature_settings_ratings_summary
import com.divinelink.feature.settings.resources.feature_settings_region
import com.divinelink.feature.settings.resources.feature_settings_streaming_services
import com.divinelink.feature.settings.resources.feature_settings_streaming_services_subtitle
import com.divinelink.feature.settings.resources.feature_settings_tv_rating_preference
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailPreferencesSettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigateUp: () -> Unit,
  viewModel: DetailsPreferencesViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(Res.string.feature_settings_details_preferences),
    onNavigationClick = onNavigateUp,
  ) {
    ScenePeekLazyColumn(
      modifier = Modifier.testTag(TestTags.LAZY_COLUMN),
    ) {
      item {
        SettingsSwitchItem(
          title = stringResource(Res.string.feature_settings_streaming_services),
          summary = stringResource(Res.string.feature_settings_streaming_services_subtitle),
          isChecked = uiState.detailPreferences.streamingServicesVisible,
          onCheckedChange = viewModel::setStreamingServicesVisible,
        )
      }

      item {
        SettingsRadioPrefItem(
          title = stringResource(Res.string.feature_settings_region),
          icon = null,
          listItems = Country.entries.sortedBy { it.name },
          selectedOption = uiState.detailPreferences.region,
          displayText = { stringResource(it.nameRes) },
          onSelected = viewModel::setRegion,
        )
      }

      item {
        HorizontalDivider()
      }

      item {
        SettingsTextItem(
          title = stringResource(Res.string.feature_settings_ratings),
          summary = stringResource(Res.string.feature_settings_ratings_summary),
        )
      }

      item {
        SettingsRadioPrefItem(
          title = stringResource(Res.string.feature_settings_movie_rating_preference),
          selectedOption = uiState.movieSource,
          displayText = { it.value },
          listItems = MediaRatingSource.Movie.options,
          onSelected = { option ->
            viewModel.setMediaRatingSource(MediaRatingSource.Movie to option)
          },
        )
      }

      item {
        SettingsRadioPrefItem(
          title = stringResource(Res.string.feature_settings_tv_rating_preference),
          selectedOption = uiState.tvSource,
          displayText = { it.value },
          listItems = MediaRatingSource.TVShow.options,
          onSelected = { option ->
            viewModel.setMediaRatingSource(MediaRatingSource.TVShow to option)
          },
        )
      }
    }
  }
}
