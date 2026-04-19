package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.details.media.DetailsForm
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.divinelink.feature.details.media.ui.forms.about.AboutFormContent
import com.divinelink.feature.details.media.ui.forms.cast.CastFormContent
import com.divinelink.feature.details.media.ui.forms.recommendation.RecommendationsFormContent
import com.divinelink.feature.details.media.ui.forms.reviews.ReviewsFormContent
import com.divinelink.feature.details.media.ui.forms.seasons.SeasonsFormContent

@Composable
fun MediaDetailsPager(
  modifier: Modifier = Modifier,
  pagerState: PagerState,
  scroll: NestedScrollConnection,
  uiState: DetailsViewState,
  onNavigate: (Navigation) -> Unit,
  mediaDetails: MediaDetails,
  onPersonClick: (Person) -> Unit,
  obfuscateEpisodes: Boolean,
  viewAllCreditsClick: () -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
  onMediaItemClick: (MediaItem) -> Unit,
) {
  HorizontalPager(
    modifier = modifier
      .background(MaterialTheme.colorScheme.background),
    state = pagerState,
  ) { page ->
    uiState.forms.values.elementAt(page).let { form ->
      when (form) {
        DetailsForm.Error -> Column(
          modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = MaterialTheme.dimensions.keyline_16)
            .nestedScroll(scroll)
            .verticalScroll(rememberScrollState()),
        ) {
          BlankSlate(uiState = BlankSlateState.Contact)
        }

        DetailsForm.Loading -> LoadingContent(
          modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scroll)
            .verticalScroll(rememberScrollState()),
        )

        is DetailsForm.Content<*> -> when (form.data) {
          is DetailsData.About -> AboutFormContent(
            modifier = Modifier.fillMaxSize().nestedScroll(scroll),
            aboutData = form.data as DetailsData.About,
            watchProviders = uiState.watchProviders,
            onNavigate = onNavigate,
          )
          is DetailsData.Cast -> CastFormContent(
            modifier = Modifier.fillMaxSize().nestedScroll(scroll),
            cast = form.data as DetailsData.Cast,
            title = mediaDetails.title,
            onPersonClick = onPersonClick,
            obfuscateSpoilers = obfuscateEpisodes,
            onViewAllClick = viewAllCreditsClick,
          )
          is DetailsData.Recommendations -> RecommendationsFormContent(
            modifier = Modifier.fillMaxSize().nestedScroll(scroll),
            recommendations = form.data as DetailsData.Recommendations,
            title = mediaDetails.title,
            onSwitchPreferences = onSwitchPreferences,
            onItemClick = onMediaItemClick,
            onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
          )
          is DetailsData.Reviews -> ReviewsFormContent(
            modifier = Modifier.fillMaxSize().nestedScroll(scroll),
            title = mediaDetails.title,
            reviews = form.data as DetailsData.Reviews,
          )
          is DetailsData.Seasons -> SeasonsFormContent(
            modifier = Modifier.fillMaxSize().nestedScroll(scroll),
            title = mediaDetails.title,
            reviews = form.data as DetailsData.Seasons,
            onClick = { seasonNumber ->
              onNavigate(
                Navigation.SeasonRoute(
                  showId = mediaDetails.id,
                  backdropPath = mediaDetails.backdropPath,
                  title = mediaDetails.title,
                  seasonNumber = seasonNumber,
                ),
              )
            },
          )
        }
      }
    }
  }
}
