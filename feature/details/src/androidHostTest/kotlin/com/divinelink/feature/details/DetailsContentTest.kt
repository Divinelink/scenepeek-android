package com.divinelink.feature.details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.fixtures.details.credits.SeriesCastFactory
import com.divinelink.core.fixtures.details.media.DetailsDataFactory
import com.divinelink.core.fixtures.details.media.DetailsFormFactory
import com.divinelink.core.fixtures.details.review.ReviewFactory
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.fixtures.model.account.AccountMediaDetailsFactory
import com.divinelink.core.fixtures.model.account.AccountMediaDetailsFactory.toWizard
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.details.rating.RatingCountFactory
import com.divinelink.core.fixtures.model.details.video.VideoFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.resources.fatal_error_fetching_details
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstanceDetailsUseCase
import com.divinelink.core.testing.usecase.TestGetServerInstancesUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.TestTags.LOADING_CONTENT
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_add_to_watchlist_content_desc
import com.divinelink.core.ui.resources.core_ui_hide_total_episodes_item
import com.divinelink.core.ui.resources.core_ui_mark_as_favorite_button_content_description
import com.divinelink.core.ui.resources.core_ui_okay
import com.divinelink.core.ui.resources.core_ui_remove_from_watchlist_content_desc
import com.divinelink.core.ui.resources.core_ui_share
import com.divinelink.core.ui.resources.core_ui_show_total_episodes_item
import com.divinelink.core.ui.resources.core_ui_tmdb_user_score
import com.divinelink.feature.details.media.ui.DetailsContent
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.details__add_rating
import com.divinelink.feature.details.resources.feature_details_request
import com.divinelink.feature.request.media.RequestMediaEntryData
import com.divinelink.feature.request.media.RequestMediaViewModel
import com.google.common.truth.Truth.assertThat
import org.jetbrains.compose.resources.getString
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class DetailsContentTest : ComposeTest() {

  private val reviews = ReviewFactory.ReviewList()

  @BeforeTest
  fun setup() {
    startKoin {
      // Do nothing
    }
  }

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun clickMarkAsFavoriteTest() = uiTest {
    var hasClickedMarkAsFavorite = false
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
        ),
        onMarkAsFavoriteClicked = {
          hasClickedMarkAsFavorite = true
        },
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    val markAsFavoriteContentDescription =
      getString(UiString.core_ui_mark_as_favorite_button_content_description)

    onNodeWithContentDescription(markAsFavoriteContentDescription)
      .performClick()

    assertThat(hasClickedMarkAsFavorite).isTrue()
  }

  @Test
  fun loadingTest() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          isLoading = true,
          mediaType = MediaType.MOVIE,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(LOADING_CONTENT).assertIsDisplayed()
  }

  @Test
  fun `test reviews form is empty when reviews are empty`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
          selectedTabIndex = MovieTab.Reviews.order,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Reviews.FORM).assertIsDisplayed()
  }

  @Test
  fun `test render movie reviews`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          selectedTabIndex = MovieTab.Reviews.order,
          forms = DetailsFormFactory.Movie.full(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Reviews.FORM).assertIsDisplayed()

    onNodeWithTag(TestTags.Details.Reviews.REVIEW_CARD.format(reviews.first().content))
      .assertIsDisplayed()
  }

  @Test
  fun testErrorAlert() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          error = UIText.ResourceText(
            com.divinelink.core.model.resources.Res.string.fatal_error_fetching_details,
          ),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    val errorText = getString(
      com.divinelink.core.model.resources.Res.string.fatal_error_fetching_details,
    )

    val okText = getString(UiString.core_ui_okay)

    onNodeWithText(errorText)
      .assertIsDisplayed()

    onNodeWithText(okText)
      .performClick()
  }

  @Test
  fun `test render watch trailer button with trailer is available`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          trailer = VideoFactory.Youtube(),
          forms = DetailsFormFactory.Movie.empty(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    // It finds two elements with the same tag, because we are using a SubcomposeLayout that
    // measures the content size.

    onAllNodesWithTag(TestTags.Details.WATCH_TRAILER).onLast().assertExists()
  }

  @Test
  fun `given user rating score is displayed`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          userDetails = AccountMediaDetailsFactory.Rated(),
          mediaDetails = MediaDetailsFactory.FightClub(),
          forms = DetailsFormFactory.Movie.full(),
          tabs = MovieTab.entries,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    val userScore = getString(UiString.core_ui_tmdb_user_score)

    onAllNodesWithText(userScore)
      .onLast()
      .assertExists()

    onAllNodesWithText("30.4k")
      .onLast()
      .assertExists()

    onAllNodesWithTag(
      testTag = TestTags.Details.YOUR_RATING.format("8"),
      useUnmergedTree = true,
    )
      .onLast()
      .assertExists()
  }

  @Test
  fun `given unrated movie add your rate button is displayed`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          forms = DetailsFormFactory.Movie.empty(),
          tabs = MovieTab.entries,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    val addYourRate = getString(Res.string.details__add_rating)

    onAllNodesWithText(
      text = addYourRate,
      useUnmergedTree = true,
    )
      .onLast()
      .assertExists()
  }

  @Test
  fun `given movie is not on watchlist add to watchlist button is displayed`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
          userDetails = AccountMediaDetailsFactory.NotRated(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithContentDescription(
      label = getString(UiString.core_ui_add_to_watchlist_content_desc),
    )
      .onLast()
      .assertExists()
  }

  @Test
  fun `given movie is on watchlist added to watchlist button is displayed`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withWatchlist(true)
          },
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithContentDescription(
      label = getString(UiString.core_ui_remove_from_watchlist_content_desc),
    )
      .onLast()
      .assertExists()
  }

  @Test
  fun `test addToWatchlist button updates button state`() = uiTest {
    var hasClickedAddToWatchlist = false
    val viewState = mutableStateOf(
      DetailsViewState(
        mediaId = 0,
        mediaType = MediaType.MOVIE,
        mediaDetails = MediaDetailsFactory.FightClub(),
        tabs = MovieTab.entries,
        forms = DetailsFormFactory.Movie.empty(),
        userDetails = AccountMediaDetailsFactory.NotRated(),
      ),
    )

    setVisibilityScopeContent {
      DetailsContent(
        viewState = viewState.value,
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {
          hasClickedAddToWatchlist = true
          viewState.value = viewState.value.copy(
            userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
              withWatchlist(true)
            },
          )
        },
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithContentDescription(
      label = getString(UiString.core_ui_add_to_watchlist_content_desc),
    )
      .onFirst()
      .assertIsDisplayed()
      .performClick()

    onAllNodesWithContentDescription(
      label = getString(UiString.core_ui_add_to_watchlist_content_desc),
    )
      .onFirst()
      .assertIsNotDisplayed()

    onAllNodesWithContentDescription(
      label = getString(UiString.core_ui_remove_from_watchlist_content_desc),
    )
      .onFirst()
      .assertIsDisplayed()

    assertThat(hasClickedAddToWatchlist).isTrue()
  }

  @Test
  fun `test open and close dropdown menu`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
    onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertIsDisplayed()
    onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
    onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertDoesNotExist()
  }

  @Test
  fun `test open and close share dialog`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
          menuOptions = listOf(DetailsMenuOptions.SHARE),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
    onNodeWithTag(TestTags.Menu.item(getString(UiString.core_ui_share)))
      .assertIsDisplayed()
      .performClick()
  }

  @Test
  fun `test dropdown menu is not available without media details`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = null,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
    onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertDoesNotExist()
  }

  @Test
  fun `test open request dialog for tv show`() = uiTest {
    val getServerInstancesUseCase = TestGetServerInstancesUseCase()
    val getServerInstanceDetailsUseCase = TestGetServerInstanceDetailsUseCase()
    val jellyseerrRepository = TestJellyseerrRepository()
    val mediaRepository = TestMediaRepository()

    jellyseerrRepository.mockGetTvDetails(response = JellyseerrMediaInfoFactory.Tv.emptyRequests())
    mediaRepository.mockFetchTvSeasons(response = Result.success(emptyList()))

    getServerInstancesUseCase.mockResponse(
      Result.success(SonarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(SonarrInstanceDetailsFactory.sonarr),
    )

    declare {
      RequestMediaViewModel(
        data = RequestMediaEntryData(request = null, media = MediaItemFactory.theOffice()),
        requestMediaUseCase = FakeRequestMediaUseCase().mock,
        getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
        authRepository = TestAuthRepository().mock,
        jellyseerrRepository = jellyseerrRepository.mock,
        mediaRepository = mediaRepository.mock,
        getServerInstancesUseCase = getServerInstancesUseCase.mock,
      )
    }
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          actionButtons = listOf(
            DetailActionItem.Rate,
            DetailActionItem.Watchlist,
            DetailActionItem.Request,
          ),
          mediaDetails = MediaDetailsFactory.TheOffice(),
          forms = DetailsFormFactory.Tv.empty(),
          tabs = TvTab.entries,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithText(getString(Res.string.feature_details_request)).assertIsNotDisplayed()
    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON).performClick()

    onNodeWithText(getString(Res.string.feature_details_request))
      .assertIsDisplayed()
      .performClick()

    onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
  }

  @Test
  fun `test open request dialog for movie`() = uiTest {
    val getServerInstancesUseCase = TestGetServerInstancesUseCase()
    val getServerInstanceDetailsUseCase = TestGetServerInstanceDetailsUseCase()

    getServerInstancesUseCase.mockResponse(
      Result.success(RadarrInstanceFactory.all),
    )

    getServerInstanceDetailsUseCase.mockResponse(
      Result.success(RadarrInstanceDetailsFactory.radarr),
    )

    declare {
      RequestMediaViewModel(
        data = RequestMediaEntryData(request = null, media = MediaItemFactory.FightClub()),
        requestMediaUseCase = FakeRequestMediaUseCase().mock,
        authRepository = TestAuthRepository().mock,
        getServerInstanceDetailsUseCase = getServerInstanceDetailsUseCase.mock,
        getServerInstancesUseCase = getServerInstancesUseCase.mock,
        jellyseerrRepository = TestJellyseerrRepository().mock,
        mediaRepository = TestMediaRepository().mock,
      )
    }

    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          actionButtons = listOf(
            DetailActionItem.Rate,
            DetailActionItem.Watchlist,
            DetailActionItem.Request,
          ),
          mediaDetails = MediaDetailsFactory.FightClub(),
          forms = DetailsFormFactory.Movie.empty(),
          tabs = MovieTab.entries,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }
    onNodeWithText(getString(Res.string.feature_details_request))
      .assertIsNotDisplayed()

    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON)
      .performClick()

    onNodeWithText(getString(Res.string.feature_details_request))
      .assertIsDisplayed()
      .performClick()

    onNodeWithTag(TestTags.Modal.REQUEST_MOVIE).assertIsDisplayed()
  }

  @Test
  fun `test on obfuscate spoilers when initial is shown`() = uiTest {
    var hasClickedObfuscateSpoilers = false
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
          menuOptions = listOf(DetailsMenuOptions.OBFUSCATE_SPOILERS),
          spoilersObfuscated = false,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {
          hasClickedObfuscateSpoilers = true
        },
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
    onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertIsDisplayed()
    onNodeWithTag(
      TestTags.Menu.item(getString(UiString.core_ui_hide_total_episodes_item)),
    )
      .assertIsDisplayed()
      .performClick()

    assertThat(hasClickedObfuscateSpoilers).isTrue()
  }

  @Test
  fun `test on obfuscate spoilers when initially is hidden`() = uiTest {
    var hasClickedObfuscateSpoilers = false
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
          menuOptions = listOf(DetailsMenuOptions.OBFUSCATE_SPOILERS),
          spoilersObfuscated = true,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {
          hasClickedObfuscateSpoilers = true
        },
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
    onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertIsDisplayed()
    onNodeWithTag(
      TestTags.Menu.item(getString(UiString.core_ui_show_total_episodes_item)),
    )
      .assertIsDisplayed()
      .performClick()

    assertThat(hasClickedObfuscateSpoilers).isTrue()
  }

  @Test
  fun `test viewAllRatingsClick`() = uiTest {
    var hasClickedViewAllRatings = false

    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          forms = DetailsFormFactory.Movie.empty(),
          tabs = MovieTab.entries,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
        onShowAllRatingsClick = {
          hasClickedViewAllRatings = true
        },
      )
    }

    onAllNodesWithTag(TestTags.Rating.DETAILS_RATING_BUTTON)
      .onFirst()
      .performClick()

    assertThat(hasClickedViewAllRatings).isTrue()
  }

  @Test
  fun `test rating item with IMDB preferences shows IMDB Rating`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub().copy(
            ratingCount = RatingCountFactory.full(),
          ),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
          ratingSource = RatingSource.IMDB,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithTag(
      testTag = TestTags.Rating.IMDB_RATING,
      useUnmergedTree = true,
    )
      .onFirst()
      .assertExists()

    onAllNodesWithText("8.5").onFirst().assertIsDisplayed()
    onAllNodesWithText(" / 10").onFirst().assertIsDisplayed()
  }

  @Test
  fun `test rating item with Trakt preferences shows Trakt Rating`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub().copy(
            ratingCount = RatingCountFactory.full(),
          ),
          ratingSource = RatingSource.TRAKT,
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithTag(
      testTag = TestTags.Rating.TRAKT_RATING,
      useUnmergedTree = true,
    )
      .onFirst()
      .assertExists()

    onAllNodesWithText("95%", useUnmergedTree = true).onFirst().assertIsDisplayed()
  }

  @Test
  fun `test rating item with tmdb preferences shows tmdb Rating`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub().copy(
            ratingCount = RatingCountFactory.full(),
          ),
          tabs = MovieTab.entries,
          forms = DetailsFormFactory.Movie.empty(),
          ratingSource = RatingSource.TMDB,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithTag(
      testTag = TestTags.Rating.TMDB_RATING,
      useUnmergedTree = true,
    )
      .onFirst()
      .assertIsDisplayed()

    onAllNodesWithText("7.5", useUnmergedTree = true).onFirst().assertIsDisplayed()
  }

  @Test
  fun `test tv status is visible when is not unknown`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            information = MediaDetailsFactory.TheOffice().information.copy(
              status = TvStatus.RETURNING_SERIES,
            ),
          ),
          tabs = TvTab.entries,
          forms = DetailsFormFactory.Tv.empty(),
          ratingSource = RatingSource.TMDB,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithTag(
      testTag = TestTags.Rating.TMDB_RATING,
      useUnmergedTree = true,
    )
      .onLast()
      .assertExists()

    onAllNodesWithText(" • Continuing")
      .onLast()
      .assertExists()
  }

  @Test
  fun `test tv status is not visible when is unknown`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice().copy(
            information = MediaDetailsFactory.TheOffice().information.copy(
              status = TvStatus.UNKNOWN,
            ),
          ),
          tabs = TvTab.entries,
          forms = DetailsFormFactory.Tv.empty(),
          ratingSource = RatingSource.TMDB,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithTag(
      testTag = TestTags.Rating.TMDB_RATING,
      useUnmergedTree = true,
    )
      .onFirst()
      .assertIsDisplayed()

    onAllNodesWithText(" • Continuing").onFirst().assertIsNotDisplayed()
  }

  @Test
  fun `test number of seasons is visible when available`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          forms = DetailsFormFactory.Tv.empty(),
          ratingSource = RatingSource.TMDB,
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onAllNodesWithText(" • 9 seasons").onFirst().assertIsDisplayed()
  }

  @Test
  fun `test about form for movies with data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          selectedTabIndex = MovieTab.About.order,
          forms = DetailsFormFactory.Movie.full(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.About.FORM).assertIsDisplayed()
  }

  @Test
  fun `test about form for tv shows with data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          selectedTabIndex = TvTab.About.order,
          forms = DetailsFormFactory.Tv.full(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.About.FORM).assertIsDisplayed()
  }

  @Test
  fun `test cast form for movies with data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          selectedTabIndex = MovieTab.Cast.order,
          forms = DetailsFormFactory.Movie.full(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Cast.FORM)
      .assertIsDisplayed()
      .performScrollToNode(
        hasText(DetailsDataFactory.Movie.cast().items.first().name),
      )

    onNodeWithText(DetailsDataFactory.Movie.cast().items.first().name).assertIsDisplayed()
  }

  @Test
  fun `test cast form for tv with data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          selectedTabIndex = TvTab.Cast.order,
          forms = DetailsFormFactory.Tv.full(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Cast.FORM)
      .assertIsDisplayed()
      .performScrollToIndex(1)

    onNodeWithText(SeriesCastFactory.cast().first().name).assertIsDisplayed()
  }

  @Test
  fun `test cast form for tv with empty data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          selectedTabIndex = TvTab.Cast.order,
          forms = DetailsFormFactory.Tv.empty(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Cast.EMPTY).assertIsDisplayed()
  }

  @Test
  fun `test recommendations form for movies with data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          tabs = MovieTab.entries,
          selectedTabIndex = MovieTab.Recommendations.order,
          forms = DetailsFormFactory.Movie.full(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Recommendations.FORM)
      .assertIsDisplayed()
      .performScrollToIndex(1)

    onNodeWithText(MediaItemFactory.MoviesList().first().name).assertIsDisplayed()
  }

  @Test
  fun `test recommendations form for tv with data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          selectedTabIndex = TvTab.Recommendations.order,
          forms = DetailsFormFactory.Tv.full(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Recommendations.FORM)
      .assertIsDisplayed()
      .performScrollToIndex(1)
    onNodeWithText(MediaItemFactory.TVList().first().name).assertIsDisplayed()
  }

  @Test
  fun `test recommendations form for tv with empty data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          selectedTabIndex = TvTab.Recommendations.order,
          forms = DetailsFormFactory.Tv.empty(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Recommendations.EMPTY).assertIsDisplayed()
  }

  @Test
  fun `test seasons form for tv with data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          selectedTabIndex = TvTab.Seasons.order,
          forms = DetailsFormFactory.Tv.full(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Seasons.FORM)
      .assertIsDisplayed()
      .performScrollToIndex(1)

    onNodeWithText(SeasonFactory.season1().name).assertIsDisplayed()
  }

  @Test
  fun `test seasons form for tv with empty data`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          selectedTabIndex = TvTab.Seasons.order,
          forms = DetailsFormFactory.Tv.empty(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Details.Seasons.EMPTY).assertIsDisplayed()
  }

  @Test
  fun `test loading forms for shows loading indicator`() = uiTest {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          mediaDetails = MediaDetailsFactory.TheOffice(),
          tabs = TvTab.entries,
          selectedTabIndex = TvTab.Seasons.order,
          forms = DetailsFormFactory.Tv.loading(),
        ),
        onMarkAsFavoriteClicked = {},
        onMediaItemClick = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        onUpdateMediaInfo = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        onSwitchPreferences = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(LOADING_CONTENT).assertIsDisplayed()
  }
}
