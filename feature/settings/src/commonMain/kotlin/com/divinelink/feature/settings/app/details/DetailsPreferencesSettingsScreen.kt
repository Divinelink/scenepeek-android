package com.divinelink.feature.settings.app.details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.model.details.rating.MediaRatingSource
import com.divinelink.feature.settings.components.SettingsRadioPrefItem
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.components.SettingsTextItem
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_details_preferences
import com.divinelink.feature.settings.resources.feature_settings_movie_rating_preference
import com.divinelink.feature.settings.resources.feature_settings_ratings
import com.divinelink.feature.settings.resources.feature_settings_ratings_summary
import com.divinelink.feature.settings.resources.feature_settings_tv_rating_preference
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailPreferencesSettingsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  onNavigateUp: () -> Unit,
  viewModel: DetailsPreferencesViewModel = koinViewModel(),
) {
  val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(Res.string.feature_settings_details_preferences),
    onNavigationClick = onNavigateUp,
  ) {
    ScenePeekLazyColumn {
      item {
        SettingsTextItem(
          title = stringResource(Res.string.feature_settings_ratings),
          summary = stringResource(Res.string.feature_settings_ratings_summary),
        )
      }

      item {
        SettingsRadioPrefItem(
          title = stringResource(Res.string.feature_settings_movie_rating_preference),
          selected = uiState.movieSource.value,
          selectedIndex = MediaRatingSource.Movie.options.indexOf(uiState.movieSource),
          listItems = MediaRatingSource.Movie.options.map { it.value },
          onSelected = { index ->
            viewModel.setMediaRatingSource(
              MediaRatingSource.Movie to MediaRatingSource.Movie.options[index],
            )
          },
        )
      }

      item {
        SettingsRadioPrefItem(
          title = stringResource(Res.string.feature_settings_tv_rating_preference),
          selected = uiState.tvSource.value,
          selectedIndex = MediaRatingSource.TVShow.options.indexOf(uiState.tvSource),
          listItems = MediaRatingSource.TVShow.options.map { it.value },
          onSelected = { index ->
            viewModel.setMediaRatingSource(
              MediaRatingSource.TVShow to MediaRatingSource.TVShow.options[index],
            )
          },
        )
      }
    }
  }
}
