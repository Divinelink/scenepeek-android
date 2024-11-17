package com.divinelink.ui.details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.model.details.MediaDetailsFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.TestTags.LOADING_CONTENT
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.details.reviews.REVIEWS_LIST
import com.divinelink.core.ui.components.details.videos.VIDEO_PLAYER_TAG
import com.divinelink.factories.ReviewFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory.toWizard
import com.divinelink.feature.details.media.ui.DetailsContent
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.divinelink.scenepeek.R
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR
import com.divinelink.feature.details.R as detailsR

class DetailsContentTest : ComposeTest() {

  @Test
  fun clickMarkAsFavoriteTest() {
    var hasClickedMarkAsFavorite = false
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {
          hasClickedMarkAsFavorite = true
        },
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
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
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          isLoading = true,
          mediaType = MediaType.MOVIE,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    composeTestRule
      .onNodeWithTag(LOADING_CONTENT)
      .assertIsDisplayed()
  }

  @Test
  fun renderReviewsWithoutMovieDetailsTest() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          reviews = reviews,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    composeTestRule
      .onNodeWithTag(REVIEWS_LIST)
      .assertDoesNotExist()
  }

  @Test
  fun renderReviewsTest() = runTest {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          reviews = reviews,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    val reviewsTitle = composeTestRule.activity.getString(uiR.string.details__reviews)

    composeTestRule
      .onNodeWithTag(TestTags.Details.CONTENT_LIST)
      .performScrollToNode(
        hasText(reviewsTitle),
      )
      .assertIsDisplayed()

    with(composeTestRule) {
      onNodeWithTag(REVIEWS_LIST).assertIsDisplayed()
    }

    composeTestRule
      .onAllNodesWithText(reviews[0].content)[0]
      .performClick()
      .assertIsDisplayed()
  }

  @Test
  fun testErrorAlert() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          error = UIText.ResourceText(detailsR.string.details__fatal_error_fetching_details),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    val errorText = composeTestRule.activity
      .getString(detailsR.string.details__fatal_error_fetching_details)

    val okText = composeTestRule.activity
      .getString(R.string.ok)

    composeTestRule
      .onNodeWithText(errorText)
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText(okText)
      .performClick()
  }

  @Test
  fun renderTrailerTest() {
    val youtubeTrailer = Video(
      id = "123",
      key = "123",
      name = "Trailer",
      site = VideoSite.YouTube,
      officialTrailer = true,
    )
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          trailer = youtubeTrailer,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    composeTestRule
      .onNodeWithTag(VIDEO_PLAYER_TAG)
      .assertIsDisplayed()
  }

  @Test
  fun `given user rating rating score is displayed`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          userDetails = AccountMediaDetailsFactory.Rated(),
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    val userScore = composeTestRule.activity.getString(detailsR.string.details__user_score, "7.3")

    composeTestRule
      .onNodeWithTag(
        testTag = TestTags.Details.YOUR_RATING,
        useUnmergedTree = true,
      )
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText(userScore)
      .assertIsDisplayed()
  }

  @Test
  fun `given unrated movie add your rate button is displayed`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    val addYourRate = composeTestRule.activity.getString(detailsR.string.details__add_rating)

    composeTestRule
      .onNodeWithText(
        text = addYourRate,
        useUnmergedTree = true,
      )
      .assertIsDisplayed()
  }

  @Test
  fun `given movie is not on watchlist add to watchlist button is displayed`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          userDetails = AccountMediaDetailsFactory.NotRated(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    val addToWatchlist = composeTestRule.activity.getString(
      uiR.string.details__add_to_watchlist_button,
    )

    composeTestRule
      .onNodeWithText(
        text = addToWatchlist,
        useUnmergedTree = true,
      )
      .assertIsDisplayed()
  }

  @Test
  fun `given movie is on watchlist added to watchlist button is displayed`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withWatchlist(true)
          },
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    val addedToWatchlist = composeTestRule.activity.getString(
      uiR.string.details__added_to_watchlist_button,
    )

    composeTestRule
      .onNodeWithText(
        text = addedToWatchlist,
        useUnmergedTree = true,
      )
      .assertIsDisplayed()
  }

  @Test
  fun `test addToWatchlist button updates button state`() {
    var hasClickedAddToWatchlist = false
    val viewState = mutableStateOf(
      DetailsViewState(
        mediaId = 0,
        mediaType = MediaType.MOVIE,
        mediaDetails = MediaDetailsFactory.FightClub(),
        userDetails = AccountMediaDetailsFactory.NotRated(),
      ),
    )

    setContentWithTheme {
      DetailsContent(
        viewState = viewState.value,
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {
          hasClickedAddToWatchlist = true
          viewState.value = viewState.value.copy(
            userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
              withWatchlist(true)
            },
          )
        },
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    val addToWatchlist = composeTestRule.activity.getString(
      uiR.string.details__add_to_watchlist_button,
    )

    with(composeTestRule) {
      onNodeWithText(
        text = addToWatchlist,
        useUnmergedTree = true,
      ).performClick()

      onNodeWithText(
        text = addToWatchlist,
        useUnmergedTree = true,
      ).assertDoesNotExist()

      onNodeWithText(
        text = composeTestRule.activity.getString(
          uiR.string.details__added_to_watchlist_button,
        ),
        useUnmergedTree = true,
      ).assertIsDisplayed()
    }
    assertThat(hasClickedAddToWatchlist).isTrue()
  }

  @Test
  fun `test open and close dropdown menu`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
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
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          menuOptions = listOf(DetailsMenuOptions.SHARE),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
      onNodeWithTag(
        TestTags.Menu.MENU_ITEM.format(getString(uiR.string.core_ui_share)),
      )
        .assertIsDisplayed()
        .performClick()
    }
  }

  @Test
  fun `test dropdown menu is not available without media details`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = null,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Menu.MENU_BUTTON_VERTICAL).performClick()
      onNodeWithTag(TestTags.Menu.DROPDOWN_MENU).assertDoesNotExist()
    }
  }

  @Test
  fun `test open request dialog for tv show`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.TV,
          actionButtons = DetailActionItem.entries,
          mediaDetails = MediaDetailsFactory.TheOffice(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
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

    composeTestRule.onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
  }

  @Test
  fun `test open request dialog for movie`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          actionButtons = DetailActionItem.entries,
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {},
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

    composeTestRule.onNodeWithTag(TestTags.Dialogs.REQUEST_MOVIE_DIALOG).assertIsDisplayed()
  }

  @Test
  fun `test on obfuscate spoilers when initial is shown`() {
    var hasClickedObfuscateSpoilers = false
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          menuOptions = listOf(DetailsMenuOptions.OBFUSCATE_SPOILERS),
          spoilersObfuscated = false,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {
          hasClickedObfuscateSpoilers = true
        },
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
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          mediaId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          menuOptions = listOf(DetailsMenuOptions.OBFUSCATE_SPOILERS),
          spoilersObfuscated = true,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
        requestMedia = {},
        viewAllCreditsClicked = {},
        onPersonClick = {},
        onObfuscateSpoilers = {
          hasClickedObfuscateSpoilers = true
        },
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

  private val reviews = ReviewFactory.ReviewList()
}
