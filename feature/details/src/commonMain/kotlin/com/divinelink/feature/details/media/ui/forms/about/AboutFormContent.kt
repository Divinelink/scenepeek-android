package com.divinelink.feature.details.media.ui.forms.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import com.divinelink.core.commons.util.encodeToString
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.details.media.MediaDetailsInformation
import com.divinelink.core.model.details.provider.WatchProviders
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.toPersonRoute
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.details.cast.CreatorsItem
import com.divinelink.feature.details.media.ui.components.GenresSection
import com.divinelink.feature.details.media.ui.components.KeywordsSection
import com.divinelink.feature.details.media.ui.components.MovieInformationSection
import com.divinelink.feature.details.media.ui.components.TvInformationSection
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AboutFormContent(
  modifier: Modifier = Modifier,
  aboutData: DetailsData.About,
  watchProviders: WatchProviders?,
  onNavigate: (Navigation) -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = modifier.testTag(TestTags.Details.About.FORM),
    contentPadding = PaddingValues(top = MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    aboutData.tagline?.let {
      item {
        Text(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          text = it,
          style = MaterialTheme.typography.bodySmall,
          fontStyle = FontStyle.Italic,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    if (!aboutData.overview.isNullOrEmpty()) {
      item {
        Text(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          text = aboutData.overview!!,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
      item {
        HorizontalDivider(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
        )
      }
    }

    item {
      WatchProvidersContent(
        modifier = Modifier.animateItem(),
        watchProviders = watchProviders,
      )
    }

    aboutData.genres?.let { genres ->
      item {
        GenresSection(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          genres = genres,
          onClick = {
            onNavigate(
              Navigation.DiscoverRoute(
                entryPointUuid = Uuid.random().toHexString(),
                mediaType = aboutData.mediaType.value,
                encodedGenre = it.encodeToString(),
              ),
            )
          },
        )
      }
    }

    aboutData.creators?.let { creators ->
      item {
        CreatorsItem(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          creators = creators,
          onClick = { onNavigate(it.toPersonRoute()) },
        )
      }
    }

    aboutData.information?.let { information ->
      item {
        HorizontalDivider(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
        )
      }

      item {
        when (information) {
          is MediaDetailsInformation.Movie -> MovieInformationSection(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
            information = information,
          )
          is MediaDetailsInformation.TV -> TvInformationSection(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
            information = information,
          )
        }
      }
    }

    aboutData.collection?.let { collection ->
      item {
        CollectionBanner(
          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          collection = collection,
          onClick = {
            onNavigate(
              Navigation.CollectionRoute(
                id = collection.id,
                name = collection.name,
                backdropPath = collection.backdropPath,
                posterPath = collection.posterPath,
              ),
            )
          },
        )
      }
    }

    aboutData.keywords?.let { keywords ->
      if (keywords.isNotEmpty()) {
        item {
          HorizontalDivider(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
          )
        }
        item {
          KeywordsSection(
            keywords = keywords,
            onClick = {
              onNavigate(
                Navigation.DiscoverRoute(
                  entryPointUuid = Uuid.random().toHexString(),
                  mediaType = aboutData.mediaType.value,
                  encodedKeyword = it.encodeToString(),
                ),
              )
            },
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
    }
  }
}
