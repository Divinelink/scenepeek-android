package com.divinelink.ui.details

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
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.fixtures.details.credits.SeriesCastFactory
import com.divinelink.core.fixtures.details.media.DetailsDataFactory
import com.divinelink.core.fixtures.details.media.DetailsFormFactory
import com.divinelink.core.fixtures.details.review.ReviewFactory
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.details.rating.RatingCountFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.TestTags.LOADING_CONTENT
import com.divinelink.core.ui.UiString
import com.divinelink.factories.VideoFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory.toWizard
import com.divinelink.feature.details.media.ui.DetailsContent
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR
import com.divinelink.feature.details.R as detailsR

class DetailsContentTest : ComposeTest() {

  @Test
  fun clickMarkAsFavoriteTest() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    val markAsFavoriteContentDescription = composeTestRule.activity
      .getString(uiR.string.core_ui_mark_as_favorite_button_content_description)

    composeTestRule
      .onNodeWithContentDescription(markAsFavoriteContentDescription)
      .performClick()

    assertThat(hasClickedMarkAsFavorite).isTrue()
  }

  @Test
  fun loadingTest() {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          isLoading = true,
          mediaType = MediaType.MOVIE,
        ),
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    composeTestRule
      .onNodeWithTag(LOADING_CONTENT)
      .assertIsDisplayed()
  }

  @Test
  fun `test reviews form is empty when reviews are empty`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Reviews.FORM).assertIsDisplayed()
    }
  }

  @Test
  fun `test render movie reviews`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Reviews.FORM).assertIsDisplayed()

      onNodeWithTag(TestTags.Details.Reviews.REVIEW_CARD.format(reviews.first().content))
        .assertIsDisplayed()
    }
  }

  @Test
  fun testErrorAlert() {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          error = UIText.ResourceText(detailsR.string.details__fatal_error_fetching_details),
        ),
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    val errorText = composeTestRule.activity
      .getString(detailsR.string.details__fatal_error_fetching_details)

    val okText = composeTestRule.activity
      .getString(UiString.core_ui_okay)

    composeTestRule
      .onNodeWithText(errorText)
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText(okText)
      .performClick()
  }

  @Test
  fun `test render watch trailer button with trailer is available`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      // It finds two elements with the same tag, because we are using a SubcomposeLayout that
      // measures the content size.

      onAllNodesWithTag(TestTags.Details.WATCH_TRAILER).onLast().assertExists()
    }
  }

  @Test
  fun `given user rating score is displayed`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      val userScore = getString(uiR.string.core_ui_tmdb_user_score)

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
  }

  @Test
  fun `given unrated movie add your rate button is displayed`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    val addYourRate = composeTestRule.activity.getString(detailsR.string.details__add_rating)

    composeTestRule
      .onAllNodesWithText(
        text = addYourRate,
        useUnmergedTree = true,
      )
      .onLast()
      .assertExists()
  }

  @Test
  fun `given movie is not on watchlist add to watchlist button is displayed`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    composeTestRule
      .onAllNodesWithContentDescription(
        label = getString(uiR.string.core_ui_add_to_watchlist_content_desc),
      )
      .onLast()
      .assertExists()
  }

  @Test
  fun `given movie is on watchlist added to watchlist button is displayed`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    composeTestRule
      .onAllNodesWithContentDescription(
        label = getString(uiR.string.core_ui_remove_from_watchlist_content_desc),
      )
      .onLast()
      .assertExists()
  }

  @Test
  fun `test addToWatchlist button updates button state`() {
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
        onSimilarMovieClicked = {},
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
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onAllNodesWithContentDescription(
        label = getString(uiR.string.core_ui_add_to_watchlist_content_desc),
      )
        .onFirst()
        .assertIsDisplayed()
        .performClick()

      onAllNodesWithContentDescription(
        label = getString(uiR.string.core_ui_add_to_watchlist_content_desc),
      )
        .onFirst()
        .assertIsNotDisplayed()

      onAllNodesWithContentDescription(
        label = getString(uiR.string.core_ui_remove_from_watchlist_content_desc),
      )
        .onFirst()
        .assertIsDisplayed()
    }
    assertThat(hasClickedAddToWatchlist).isTrue()
  }

  @Test
  fun `test open and close dropdown menu`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
      onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertIsDisplayed()
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
      onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertDoesNotExist()
    }
  }

  @Test
  fun `test open and close share dialog`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
      onNodeWithTag(TestTags.Menu.MENU_ITEM.format(getString(uiR.string.core_ui_share)))
        .assertIsDisplayed()
        .performClick()
    }
  }

  @Test
  fun `test dropdown menu is not available without media details`() {
    setVisibilityScopeContent {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = null,
        ),
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
      onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertDoesNotExist()
    }
  }

  @Test
  fun `test open request dialog for tv show`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }
    composeTestRule
      .onNodeWithText(getString(detailsR.string.feature_details_request))
      .assertIsNotDisplayed()

    composeTestRule
      .onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON)
      .performClick()

    composeTestRule
      .onNodeWithText(getString(detailsR.string.feature_details_request))
      .assertIsDisplayed()
      .performClick()

    composeTestRule.onNodeWithTag(TestTags.Modal.REQUEST_SEASONS).assertIsDisplayed()
  }

  @Test
  fun `test open request dialog for movie`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }
    composeTestRule
      .onNodeWithText(getString(detailsR.string.feature_details_request))
      .assertIsNotDisplayed()

    composeTestRule
      .onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON)
      .performClick()

    composeTestRule
      .onNodeWithText(getString(detailsR.string.feature_details_request))
      .assertIsDisplayed()
      .performClick()

    composeTestRule.onNodeWithTag(TestTags.Modal.REQUEST_MOVIE).assertIsDisplayed()
  }

  @Test
  fun `test on obfuscate spoilers when initial is shown`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
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
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
      onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertIsDisplayed()
      onNodeWithTag(
        TestTags.Menu.MENU_ITEM.format(getString(uiR.string.core_ui_hide_total_episodes_item)),
      )
        .assertIsDisplayed()
        .performClick()
    }

    assertThat(hasClickedObfuscateSpoilers).isTrue()
  }

  @Test
  fun `test on obfuscate spoilers when initially is hidden`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
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
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
      onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertIsDisplayed()
      onNodeWithTag(
        TestTags.Menu.MENU_ITEM.format(getString(uiR.string.core_ui_show_total_episodes_item)),
      )
        .assertIsDisplayed()
        .performClick()
    }

    assertThat(hasClickedObfuscateSpoilers).isTrue()
  }

  @Test
  fun `test viewAllRatingsClick`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
        onShowAllRatingsClick = {
          hasClickedViewAllRatings = true
        },
      )
    }

    with(composeTestRule) {
      onAllNodesWithTag(TestTags.Rating.DETAILS_RATING_BUTTON)
        .onFirst()
        .performClick()
    }

    assertThat(hasClickedViewAllRatings).isTrue()
  }

  @Test
  fun `test rating item with IMDB preferences shows IMDB Rating`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onAllNodesWithTag(
        testTag = TestTags.Rating.IMDB_RATING,
        useUnmergedTree = true,
      )
        .onFirst()
        .assertExists()

      onAllNodesWithText("8.5").onFirst().assertIsDisplayed()
      onAllNodesWithText(" / 10").onFirst().assertIsDisplayed()
    }
  }

  @Test
  fun `test rating item with Trakt preferences shows Trakt Rating`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onAllNodesWithTag(
        testTag = TestTags.Rating.TRAKT_RATING,
        useUnmergedTree = true,
      )
        .onFirst()
        .assertExists()

      onAllNodesWithText("95%", useUnmergedTree = true).onFirst().assertIsDisplayed()
    }
  }

  @Test
  fun `test rating item with tmdb preferences shows tmdb Rating`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onAllNodesWithTag(
        testTag = TestTags.Rating.TMDB_RATING,
        useUnmergedTree = true,
      )
        .onFirst()
        .assertIsDisplayed()

      onAllNodesWithText("7.5", useUnmergedTree = true).onFirst().assertIsDisplayed()
    }
  }

  @Test
  fun `test tv status is visible when is not unknown`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
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
  }

  @Test
  fun `test tv status is not visible when is unknown`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onAllNodesWithTag(
        testTag = TestTags.Rating.TMDB_RATING,
        useUnmergedTree = true,
      )
        .onFirst()
        .assertIsDisplayed()

      onAllNodesWithText(" • Continuing").onFirst().assertIsNotDisplayed()
    }
  }

  @Test
  fun `test number of seasons is visible when available`() = runTest {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onAllNodesWithText(" • 9 seasons").onFirst().assertIsDisplayed()
    }
  }

  @Test
  fun `test about form for movies with data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.About.FORM).assertIsDisplayed()
    }
  }

  @Test
  fun `test about form for tv shows with data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.About.FORM).assertIsDisplayed()
    }
  }

  @Test
  fun `test cast form for movies with data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Cast.FORM)
        .assertIsDisplayed()
        .performScrollToNode(
          hasText(DetailsDataFactory.Movie.cast().items.first().name),
        )

      onNodeWithText(DetailsDataFactory.Movie.cast().items.first().name).assertIsDisplayed()
    }
  }

  @Test
  fun `test cast form for tv with data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Cast.FORM)
        .assertIsDisplayed()
        .performScrollToNode(hasText(SeriesCastFactory.cast().first().name))
      onNodeWithText(SeriesCastFactory.cast().first().name).assertIsDisplayed()
    }
  }

  @Test
  fun `test cast form for tv with empty data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Cast.EMPTY).assertIsDisplayed()
    }
  }

  @Test
  fun `test recommendations form for movies with data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Recommendations.FORM).assertIsDisplayed()
      onNodeWithText(MediaItemFactory.MoviesList().first().overview).assertIsDisplayed()
    }
  }

  @Test
  fun `test recommendations form for tv with data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Recommendations.FORM).assertIsDisplayed()
      onNodeWithText(MediaItemFactory.TVList().first().overview).assertIsDisplayed()
    }
  }

  @Test
  fun `test recommendations form for tv with empty data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Recommendations.EMPTY).assertIsDisplayed()
    }
  }

  @Test
  fun `test seasons form for tv with data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Seasons.FORM)
        .assertIsDisplayed()
        .performScrollToNode(
          matcher = hasText(SeasonFactory.season1().overview),
        )

      onNodeWithText(SeasonFactory.season1().overview)
        .assertIsDisplayed()
    }
  }

  @Test
  fun `test seasons form for tv with empty data`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.Seasons.EMPTY).assertIsDisplayed()
    }
  }

  @Test
  fun `test loading forms for shows loading indicator`() {
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
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClick = {},
        onAddToWatchlistClick = {},
        requestMedia = {},
        onViewAllCreditsClick = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
        onShowAllRatingsClick = {},
        onTabSelected = {},
        onPlayTrailerClick = {},
        onDeleteRequest = {},
        onDeleteMedia = {},
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(LOADING_CONTENT).assertIsDisplayed()
    }
  }

  private val reviews = ReviewFactory.ReviewList()
}
