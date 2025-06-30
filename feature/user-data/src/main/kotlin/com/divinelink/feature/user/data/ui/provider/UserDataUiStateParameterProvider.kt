package com.divinelink.feature.user.data.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.feature.user.data.MediaTab
import com.divinelink.feature.user.data.UserDataForm
import com.divinelink.feature.user.data.UserDataUiState

class UserDataUiStateParameterProvider : PreviewParameterProvider<UserDataUiState> {
  override val values: Sequence<UserDataUiState> = sequenceOf(
    UserDataUiState(
      section = UserDataSection.Watchlist,
      selectedTabIndex = 0,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> null
          MediaTab.TV -> null
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Loading,
        MediaType.TV to UserDataForm.Loading,
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Watchlist,
      selectedTabIndex = 0,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 500
          MediaTab.TV -> 1000
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = MediaItemFactory.MoviesList(),
          totalResults = 500,
        ),
        MediaType.TV to UserDataForm.Loading,
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Watchlist,
      selectedTabIndex = 1,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 500
          MediaTab.TV -> 1000
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = MediaItemFactory.MoviesList(),
          totalResults = 500,
        ),
        MediaType.TV to UserDataForm.Data(
          mediaType = MediaType.TV,
          data = MediaItemFactory.tvAll(),
          totalResults = 500,
        ),
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Watchlist,
      selectedTabIndex = 0,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 0
          MediaTab.TV -> 0
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = emptyList(),
          totalResults = 0,
        ),
        MediaType.TV to UserDataForm.Data(
          mediaType = MediaType.TV,
          data = emptyList(),
          totalResults = 0,
        ),
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Watchlist,
      selectedTabIndex = 1,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 0
          MediaTab.TV -> 0
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = emptyList(),
          totalResults = 0,
        ),
        MediaType.TV to UserDataForm.Data(
          mediaType = MediaType.TV,
          data = emptyList(),
          totalResults = 0,
        ),
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Ratings,
      selectedTabIndex = 0,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 500
          MediaTab.TV -> 1000
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = MediaItemFactory.MoviesList(),
          totalResults = 500,
        ),
        MediaType.TV to UserDataForm.Loading,
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Ratings,
      selectedTabIndex = 1,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 500
          MediaTab.TV -> 1000
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = MediaItemFactory.MoviesList(),
          totalResults = 500,
        ),
        MediaType.TV to UserDataForm.Data(
          mediaType = MediaType.TV,
          data = MediaItemFactory.tvAll(),
          totalResults = 500,
        ),
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Ratings,
      selectedTabIndex = 1,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 500
          MediaTab.TV -> 1000
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Error.Unknown,
        MediaType.TV to UserDataForm.Error.Unauthenticated,
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Ratings,
      selectedTabIndex = 0,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 0
          MediaTab.TV -> 0
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = emptyList(),
          totalResults = 0,
        ),
        MediaType.TV to UserDataForm.Data(
          mediaType = MediaType.TV,
          data = emptyList(),
          totalResults = 0,
        ),
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
    UserDataUiState(
      section = UserDataSection.Ratings,
      selectedTabIndex = 1,
      tabs = MediaTab.entries.associateWith {
        when (it) {
          MediaTab.MOVIE -> 0
          MediaTab.TV -> 0
        }
      },
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to UserDataForm.Data(
          mediaType = MediaType.MOVIE,
          data = emptyList(),
          totalResults = 0,
        ),
        MediaType.TV to UserDataForm.Data(
          mediaType = MediaType.TV,
          data = emptyList(),
          totalResults = 0,
        ),
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
  )
}
