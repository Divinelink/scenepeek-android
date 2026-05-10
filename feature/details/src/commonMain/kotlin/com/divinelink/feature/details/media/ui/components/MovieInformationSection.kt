package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.media.MediaDetailsInformation
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.resources.revenue
import com.divinelink.core.ui.SimpleInformationRow
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_information
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_information_budget
import com.divinelink.feature.details.resources.feature_details_information_companies
import com.divinelink.feature.details.resources.feature_details_information_countries
import com.divinelink.feature.details.resources.feature_details_information_original_language
import com.divinelink.feature.details.resources.feature_details_information_original_title
import com.divinelink.feature.details.resources.feature_details_information_runtime
import com.divinelink.feature.details.resources.feature_details_information_status
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import com.divinelink.core.model.resources.Res as ModelRes

@Composable
fun MovieInformationSection(
  modifier: Modifier = Modifier,
  information: MediaDetailsInformation.Movie,
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      modifier = Modifier
        .padding(horizontal = MaterialTheme.dimensions.keyline_16)
        .padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = stringResource(UiString.core_ui_information),
      style = MaterialTheme.typography.titleMedium,
    )

    information.releaseDates?.let { releaseInfo ->
      ReleaseInformation(releaseInfo)
    }

    SimpleInformationRow(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      title = stringResource(Res.string.feature_details_information_original_title),
      data = information.originalTitle,
    )

    SimpleInformationRow(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      title = stringResource(Res.string.feature_details_information_status),
      data = information.status,
    )

    SimpleInformationRow(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      title = stringResource(Res.string.feature_details_information_runtime),
      data = information.runtime ?: "-",
    )

    SimpleInformationRow(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      title = stringResource(Res.string.feature_details_information_original_language),
      data = information.originalLanguage?.let { language ->
        stringResource(language.nameRes)
      } ?: "-",
    )

    CountriesRow(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      countries = information.countries,
    )

    CompaniesRow(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      companies = information.companies,
    )

    SimpleInformationRow(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      title = stringResource(Res.string.feature_details_information_budget),
      data = information.budget,
    )

    SimpleInformationRow(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      title = stringResource(ModelRes.string.revenue),
      data = information.revenue,
    )
  }
}

@Composable
fun CountriesRow(
  modifier: Modifier = Modifier,
  countries: List<Country>,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Text(
      modifier = Modifier.weight(0.35f),
      text = pluralStringResource(
        Res.plurals.feature_details_information_countries,
        countries.size,
      ),
      style = MaterialTheme.typography.bodyMedium,
    )

    Text(
      modifier = Modifier.weight(0.65f),
      text = countries.map {
        stringResource(it.nameRes) + " " + it.flag
      }.joinToString(separator = "\n"),
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Composable
fun CompaniesRow(
  modifier: Modifier = Modifier,
  companies: List<String>,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Text(
      modifier = Modifier.weight(0.35f),
      text = pluralStringResource(
        Res.plurals.feature_details_information_companies,
        companies.size,
      ),
      style = MaterialTheme.typography.bodyMedium,
    )

    Text(
      modifier = Modifier.weight(0.65f),
      text = companies.joinToString(separator = "\n"),
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}
