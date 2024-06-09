package com.andreolas.movierama.settings.app.help

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andreolas.movierama.BuildConfig
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.components.SettingsScaffold
import com.andreolas.movierama.settings.components.SettingsTextItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination<RootGraph>
fun HelpSettingsScreen(
  navigator: DestinationsNavigator
) {
  SettingsScaffold(
    title = stringResource(id = R.string.HelpSettingsFragment__help),
    onNavigationClick = navigator::navigateUp
  ) { paddingValues ->

    val buildVersion = if (BuildConfig.DEBUG) {
      "${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}"
    } else {
      BuildConfig.VERSION_NAME
    }

    LazyColumn(contentPadding = paddingValues) {
      item {
        SettingsTextItem(
          title = stringResource(id = R.string.HelpSettingsFragment__version),
          summary = buildVersion
        )
      }
    }
  }
}
