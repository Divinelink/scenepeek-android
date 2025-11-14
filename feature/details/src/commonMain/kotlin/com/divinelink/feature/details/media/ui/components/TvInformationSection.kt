package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.media.MediaDetailsInformation
import com.divinelink.feature.details.Res
import com.divinelink.feature.details.feature_details_information
import com.divinelink.feature.details.feature_details_information_aired_episodes
import com.divinelink.feature.details.feature_details_information_first_air_date
import com.divinelink.feature.details.feature_details_information_last_air_date
import com.divinelink.feature.details.feature_details_information_next_episode_air_date
import com.divinelink.feature.details.feature_details_information_original_language
import com.divinelink.feature.details.feature_details_information_original_title
import com.divinelink.feature.details.feature_details_information_seasons
import com.divinelink.feature.details.feature_details_information_status
import org.jetbrains.compose.resources.stringResource

@Composable
fun TvInformationSection(information: MediaDetailsInformation.TV) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = stringResource(Res.string.feature_details_information),
      style = MaterialTheme.typography.titleMedium,
    )

    SimpleInformationRow(
      title = stringResource(Res.string.feature_details_information_original_title),
      data = information.originalTitle,
    )

    if (information.status != TvStatus.UNKNOWN) {
      SimpleInformationRow(
        title = stringResource(Res.string.feature_details_information_status),
        data = stringResource(information.status.resId),
      )
    }

    SimpleInformationRow(
      title = stringResource(Res.string.feature_details_information_first_air_date),
      data = information.firstAirDate,
    )

    SimpleInformationRow(
      title = stringResource(Res.string.feature_details_information_last_air_date),
      data = information.lastAirDate,
    )

    information.nextEpisodeAirDate?.let { airDate ->
      SimpleInformationRow(
        title = stringResource(Res.string.feature_details_information_next_episode_air_date),
        data = airDate,
      )
    }

    SimpleInformationRow(
      title = stringResource(Res.string.feature_details_information_seasons),
      data = information.seasons.toString(),
    )

    SimpleInformationRow(
      title = stringResource(Res.string.feature_details_information_aired_episodes),
      data = information.episodes.toString(),
    )

    SimpleInformationRow(
      title = stringResource(Res.string.feature_details_information_original_language),
      data = information.originalLanguage?.let { language ->
        stringResource(language.nameRes)
      } ?: "-",
    )

    CountriesRow(information.countries)

    CompaniesRow(information.companies)
  }
}
