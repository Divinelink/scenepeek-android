package com.divinelink.core.ui

object TestTags {

  object Components {

    object SearchBar {
      const val LOADING_INDICATOR = "Search Bar Loading Indicator"
    }

    object TopAppBar {
      const val TOP_APP_BAR = "Top App Bar"
      const val TOP_APP_BAR_TITLE = "Top App Bar Title"
    }

    object ExpandableFab {
      const val BACKGROUND = "Floating Button Background"
      const val BUTTON = "Expandable Floating Action Button"
    }
  }

  const val LOADING_CONTENT = "LOADING_CONTENT"
  const val MEDIA_LIST_TAG = "MEDIA_LIST_TAG"
  const val SCROLL_TO_TOP_BUTTON = "SCROLL_TO_TOP_BUTTON_TAG"
  const val LOADING_PROGRESS = "Loading Progress Bar"
  const val BLANK_SLATE = "Blank Slate"

  const val LAZY_COLUMN = "Lazy Column"

  object Details {
    const val CONTENT_LIST = "Details Content Scrollable List"
    const val CONTENT_SCAFFOLD = "Details Content Scaffold"
    const val SIMILAR_MOVIES_LIST = "Details Similar Movies Lazy List"

    const val YOUR_RATING = "Details Your Rating"
    const val RATE_DIALOG = "Details Rate Dialog"
    const val RATE_SLIDER = "Details Rate Slider"
  }

  object Person {
    const val PERSONAL_DETAILS = "Person Personal Details"
    const val CONTENT_LIST = "Person Details Lazy List"

    const val SHIMMERING_BIOGRAPHY_CONTENT = "Shimmering Biography Content"

    const val KNOWN_FOR_SECTION = "Person Known For Section"
    const val KNOWN_FOR_SECTION_LIST = "Person Known For Section Lazy List"
  }

  object Dialogs {
    const val ALERT_DIALOG = "Dialogs Alert Dialog"
    const val CONFIRM_BUTTON = "Dialog Confirm Button"
    const val DISMISS_BUTTON = "Dialog Dismiss Button"

    const val SELECT_SEASONS_DIALOG = "Select Seasons Dialog"
    const val REQUEST_MOVIE_DIALOG = "Request Movie Dialog"
  }

  object RadioButton {
    const val SELECT_SEASON_RADIO_BUTTON = "Select Season %s Radio Button"
  }

  object Settings {
    const val TOP_APP_BAR = "Settings Top App Bar"
    const val NAVIGATION_ICON = "Settings Navigation Icon"

    object Account {
      const val LOGIN_BUTTON = "Account Login Button"
      const val LOGOUT_BUTTON = "Account Logout Button"
    }

    object Jellyseerr {
      const val INITIAL_BOTTOM_SHEET = "Jellyseerr Initial Bottom Sheet"
      const val LOGGED_IN_BOTTOM_SHEET = "Jellyseerr Logged In Bottom Sheet"
      const val ADDRESS_TEXT_FIELD = "Jellyseerr Address Text Field"

      const val LOGGED_IN_AVATAR = "Jellyseerr Logged In Avatar"

      const val JELLYFIN_EXPANDABLE_CARD_BUTTON = "Jellyfin Expandable Card"
      const val JELLYFIN_USERNAME_TEXT_FIELD = "Jellyfin Username"
      const val JELLYFIN_PASSWORD_TEXT_FIELD = "Jellyfin Password"

      const val JELLYSEERR_EXPANDABLE_CARD_BUTTON = "Jellyseerr Expandable Card"
      const val JELLYSEERR_USERNAME_TEXT_FIELD = "Jellyseerr Username"
      const val JELLYSEERR_PASSWORD_TEXT_FIELD = "Jellyseerr Password"

      const val JELLYSEERR_LOGIN_BUTTON = "Jellyseerr Login Button"
      const val JELLYSEERR_LOGOUT_BUTTON = "Jellyseerr Logout Button"
    }

    object LinkHandling {
      const val DIRECTIONS_TEXT = "Link Handling Directions"
    }
  }

  object Menu {
    const val MENU_BUTTON_VERTICAL = "Menu Button Vertical"
    const val DROPDOWN_MENU = "Dropdown Menu"
    const val MENU_ITEM = "Menu Item %s"
    const val EPISODES_OBFUSCATION = "Episodes Obfuscation Button"
  }

  object Watchlist {
    const val TAB_BAR = "Watchlist Tab Bar $%s"
    const val WATCHLIST_CONTENT = "Watchlist Content with data"
    const val WATCHLIST_SCREEN = "Watchlist Screen"
    const val WATCHLIST_ERROR_CONTENT = "Watchlist Error Content"
  }

  object Credits {
    const val TAB_BAR = "Credits Tab Bar $%s"
    const val CAST_CREDITS_CONTENT = "Credits Content with cast"
    const val CREW_CREDITS_CONTENT = "Credits Content with crew"
  }

  object Shimmer {
    const val HALF_LINE = "Shimmer Half Line - %s"
    const val LINE = "Shimmer Line - %s"
  }
}
