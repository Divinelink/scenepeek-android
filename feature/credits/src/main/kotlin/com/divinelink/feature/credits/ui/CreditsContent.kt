package com.divinelink.feature.credits.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
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
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import kotlinx.coroutines.launch

@Composable
fun CreditsContent(
  modifier: Modifier = Modifier,
  state: CreditsUiState,
  onTabSelected: (Int) -> Unit,
  onPersonSelected: (Person) -> Unit,
) {
  var selectedPage by rememberSaveable { mutableIntStateOf(0) }
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
            return@HorizontalPager
          }
          LazyColumn(
            modifier = Modifier.testTag(TestTags.Credits.CREDITS_CONTENT),
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
              )
            }
          }
        }
        is CreditsUiContent.Crew -> {
          if (content.crew.isEmpty()) {
            return@HorizontalPager
          }
          LazyColumn(
            modifier = Modifier.testTag(TestTags.Credits.CREDITS_CONTENT),
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
                  (it.role as PersonRole.Crew).creditId + (it.role as PersonRole.Crew).department
                },
              ) { person ->
                PersonItem(
                  person = person,
                  onClick = onPersonSelected,
                )
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
private fun CreditsContentPreview() {
  AppTheme {
    CreditsContent(
      state = CreditsUiState(
        selectedTabIndex = 0,
        tabs = listOf(
          CreditsTab.Cast(2),
          CreditsTab.Crew(1),
        ),
        forms = mapOf(
          CreditsTab.Cast(2) to CreditsUiContent.Cast(
            cast = listOf(
              Person(
                id = 1,
                name = "Person 1",
                profilePath = "https://image.tmdb.org/t/p/w185/1.jpg",
                role = PersonRole.SeriesActor(
                  character = "Character 1",
                ),
              ),
              Person(
                id = 2,
                name = "Person 2",
                profilePath = "https://image.tmdb.org/t/p/w185/2.jpg",
                role = PersonRole.SeriesActor(
                  character = "Character 2",
                ),
              ),
            ),
          ),
          CreditsTab.Crew(1) to CreditsUiContent.Crew(
            crew = listOf(
              SeriesCrewDepartment(
                department = "Department 1",
                crewList = listOf(
                  Person(
                    id = 3,
                    name = "Person 3",
                    profilePath = "https://image.tmdb.org/t/p/w185/3.jpg",
                    role = PersonRole.Crew(
                      job = "Job 3",
                      creditId = "Credit 3",
                    ),
                  ),
                  Person(
                    id = 4,
                    name = "Person 4",
                    profilePath = "https://image.tmdb.org/t/p/w185/4.jpg",
                    role = PersonRole.Crew(
                      job = "Job 4",
                      creditId = "Credit 4",
                    ),
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
      onTabSelected = {},
      onPersonSelected = { },
    )
  }
}
