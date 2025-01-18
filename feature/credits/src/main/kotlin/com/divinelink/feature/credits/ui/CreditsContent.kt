package com.divinelink.feature.credits.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.credits.provider.CreditsUiStateParameterProvider
import kotlinx.coroutines.launch

@Composable
fun CreditsContent(
  modifier: Modifier = Modifier,
  state: CreditsUiState,
  onTabSelected: (Int) -> Unit,
  onPersonSelected: (Person) -> Unit,
) {
  var selectedPage by rememberSaveable { mutableIntStateOf(state.selectedTabIndex) }
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { state.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      onTabSelected(page)
      selectedPage = page
    }
  }

  Column(modifier = modifier) {
    CreditsTabs(
      tabs = state.tabs,
      selectedIndex = state.selectedTabIndex,
      onClick = {
        onTabSelected(it)
        scope.launch { pagerState.animateScrollToPage(it) }
      },
    )

    HorizontalPager(
      modifier = Modifier.fillMaxSize(),
      state = pagerState,
    ) { page ->
      when (val content = state.forms.values.elementAt(page)) {
        is CreditsUiContent.Cast -> {
          if (content.cast.isEmpty()) {
            BlankSlate(uiState = BlankSlateState.Custom(title = content.castMissingText))
          } else {
            ScenePeekLazyColumn(
              modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.Credits.CAST_CREDITS_CONTENT),
              contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_12),
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
            ) {
              items(
                items = content.cast,
                key = { it.id },
              ) { person ->
                PersonItem(
                  person = person,
                  onClick = onPersonSelected,
                  isObfuscated = state.obfuscateSpoilers,
                )
              }
            }
          }
        }
        is CreditsUiContent.Crew -> {
          if (content.crew.isEmpty()) {
            BlankSlate(uiState = BlankSlateState.Custom(title = content.crewMissingText))
          } else {
            ScenePeekLazyColumn(
              modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.Credits.CREW_CREDITS_CONTENT),
              contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_12),
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
            ) {
              content.crew.forEach { department ->
                item {
                  Text(
                    modifier = Modifier.padding(
                      top = MaterialTheme.dimensions.keyline_12,
                      bottom = MaterialTheme.dimensions.keyline_4,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    text = department.department,
                  )
                }
                items(
                  items = department.uniqueCrewList,
                  key = {
                    (it.role.first() as PersonRole.Crew).creditId +
                      (it.role.first() as PersonRole.Crew).department
                  },
                ) { person ->
                  PersonItem(
                    person = person,
                    onClick = onPersonSelected,
                    isObfuscated = state.obfuscateSpoilers,
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Previews
@Composable
fun CreditsContentPreview(
  @PreviewParameter(CreditsUiStateParameterProvider::class) uiState: CreditsUiState,
) {
  AppTheme {
    Surface {
      CreditsContent(
        state = uiState,
        onTabSelected = { },
        onPersonSelected = { },
      )
    }
  }
}
