package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.nestedscroll.CollapsingContentNestedScrollConnection
import com.divinelink.core.ui.nestedscroll.rememberCollapsingContentNestedScrollConnection
import com.divinelink.feature.details.person.ui.credits.KnownForSection
import com.divinelink.feature.details.person.ui.provider.PersonUiStatePreviewParameterProvider
import com.divinelink.feature.details.person.ui.tab.PersonTabs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun PersonContent(
  scope: CoroutineScope,
  uiState: PersonUiState,
  connection: CollapsingContentNestedScrollConnection,
  paddingValues: PaddingValues,
  lazyListState: LazyListState,
  selectedPage: Int,
  pagerState: PagerState,
  onMediaClick: (MediaItem) -> Unit,
) {
  var selectedPage1 = selectedPage
  uiState.personDetails as PersonDetailsUiState.Data

  Box(Modifier.nestedScroll(connection)) {
    CollapsiblePersonContent(
      paddingValues = paddingValues,
      connection = connection,
      personDetails = uiState.personDetails,
    )

    ScenePeekLazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .offset {
          IntOffset(0, connection.currentSize.roundToPx())
        }
        .padding(top = paddingValues.calculateTopPadding())
        .testTag(TestTags.Person.CONTENT_LIST),
      state = lazyListState,
    ) {
      stickyHeader {
        PersonTabs(
          tabs = uiState.tabs,
          selectedIndex = selectedPage1,
          onClick = {
            selectedPage1 = it
            scope.launch {
              pagerState.animateScrollToPage(it)
            }
          },
        )
      }

      item {
        HorizontalPager(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
          state = pagerState,
        ) { page ->
          uiState.forms.values.elementAt(page).let { form ->
            when (form) {
              is PersonForm.About -> LazyColumn(
                modifier = Modifier.fillParentMaxSize(),
              ) {
                item {
                  PersonalDetails(uiState.personDetails)
                }

                item {
                  KnownForSection(
                    list = uiState.knownForCredits ?: emptyList(),
                    onMediaClick = onMediaClick,
                  )
                }
              }
              is PersonForm.Movies -> LazyColumn(
                modifier = Modifier.fillParentMaxSize(),
              ) {
                itemsIndexed(form.data) { index, item ->
                  DetailedMediaItem(mediaItem = item.mediaItem, onClick = onMediaClick)
                }
              }

              is PersonForm.TvShows -> LazyColumn(
                modifier = Modifier.fillParentMaxSize(),
              ) {
                itemsIndexed(form.data) { index, item ->
                  DetailedMediaItem(mediaItem = item.mediaItem, onClick = onMediaClick)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun CollapsiblePersonContent(
  paddingValues: PaddingValues,
  connection: CollapsingContentNestedScrollConnection,
  personDetails: PersonDetailsUiState.Data,
) {
  Column(
    modifier = Modifier
      .padding(top = paddingValues.calculateTopPadding())
      .fillMaxWidth()
      .graphicsLayer {
        alpha = (connection.currentSize / connection.maxHeight)
        translationY = -(connection.maxHeight.toPx() - connection.currentSize.toPx()) / 2f
      },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      style = MaterialTheme.typography.displaySmall,
      text = personDetails.personDetails.person.name,
      textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))

    MovieImage(
      modifier = Modifier.height(MaterialTheme.dimensions.posterSize),
      path = personDetails.personDetails.person.profilePath,
      errorPlaceHolder = if (
        personDetails.personDetails.person.gender == Gender.FEMALE
      ) {
        painterResource(id = R.drawable.core_ui_ic_female_person_placeholder)
      } else {
        painterResource(id = R.drawable.core_ui_ic_person_placeholder)
      },
    )
  }
}

@Previews
@Composable
fun PersonContentPreview(
  @PreviewParameter(PersonUiStatePreviewParameterProvider::class)
  uiState: PersonUiState,
) {
  AppTheme {
    Surface {
      PersonContent(
        uiState = uiState,
        connection = rememberCollapsingContentNestedScrollConnection(256.dp),
        paddingValues = PaddingValues(0.dp),
        lazyListState = rememberLazyListState(),
        selectedPage = 0,
        scope = rememberCoroutineScope(),
        pagerState = rememberPagerState(pageCount = { uiState.tabs.size }),
        onMediaClick = {},
      )
    }
  }
}
