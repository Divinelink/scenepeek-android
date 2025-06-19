package com.divinelink.core.ui

object TestTags {

  const val TOP_LEVEL_DESTINATION = "Top Level Destination %s"

  object Components {

    const val PERSISTENT_SCAFFOLD = "Persistent Scaffold"

    object SearchBar {
      const val CLICKABLE_SEARCH_BAR = "Clickable Search Bar"
      const val SEARCH_BAR = "Search Bar focus status %s"
      const val LOADING_INDICATOR = "Search Bar Loading Indicator"
      const val CLOSE_SEARCH = "Search Bar Close Button"
    }

    object TopAppBar {
      const val TOP_APP_BAR = "Top App Bar"
      const val TOP_APP_BAR_TITLE = "Top App Bar Title"
    }

    object ExpandableFab {
      const val BACKGROUND = "Floating Button Background"
      const val BUTTON = "Expandable Floating Action Button"
    }

    const val FILTER_BUTTON = "Filter Button"

    const val NAVIGATION_BAR = "App Navigation Bar"
  }

  const val LOADING_CONTENT = "LOADING_CONTENT"
  const val MEDIA_LIST_TAG = "MEDIA_LIST_TAG"
  const val SCROLL_TO_TOP_BUTTON = "SCROLL_TO_TOP_BUTTON_TAG"
  const val LOADING_PROGRESS = "Loading Progress Bar"
  const val BLANK_SLATE = "Blank Slate"

  const val LAZY_COLUMN = "Lazy Column"

  const val VIEW_ALL = "View All Button"

  object Rating {
    const val IMDB_RATING = "IMDb Rating"
    const val IMDB_RATING_SKELETON = "IMDb Rating Skeleton"
    const val TRAKT_RATING = "Trakt Rating"
    const val TRAKT_RATING_SKELETON = "Trakt Rating Skeleton"
    const val TMDB_RATING = "TMDb Rating"

    const val DETAILS_RATING_BUTTON = "User Score Button"
    const val VOTE_COUNT = "Rating Vote Count %s"
    const val ALL_RATINGS_BOTTOM_SHEET = "All Ratings Bottom Sheet"

    const val RATING_SOURCE_SKELETON = "Rating skeleton for %s"
  }

  object Details {
    const val CONTENT_SCAFFOLD = "Details Content Scaffold"

    const val COLLAPSIBLE_LAYOUT = "Details Collapsible Layout"
    const val COLLAPSIBLE_CONTENT = "Details Collapsible Content"

    const val RATE_THIS_BUTTON = "Rate This Button"
    const val YOUR_RATING = "Details Your Rating %s"
    const val RATE_DIALOG = "Details Rate Dialog"
    const val RATE_SLIDER = "Details Rate Slider"

    const val WATCH_TRAILER = "Details Watch Trailer Button"

    object About {
      const val FORM = "Details About Form"
    }

    object Recommendations {
      const val EMPTY = "Details Recommendations Empty"
      const val FORM = "Details Recommendations Form"
    }

    object Cast {
      const val FORM = "Details Cast Form"
      const val EMPTY = "Details Cast Empty"
    }

    object Reviews {
      const val FORM = "Details Reviews Form"
      const val REVIEW_CARD = "Details Reviews Card - %s"
      const val EMPTY = "Details Reviews Empty"
    }

    object Seasons {
      const val FORM = "Details Seasons Form"
      const val EMPTY = "Details Seasons Empty"
    }
  }

  object Tabs {
    const val TAB_ITEM = "Tab Item %s"
  }

  object Person {
    const val PERSONAL_DETAILS = "Person Personal Details"
    const val CONTENT_LIST = "Person Details Lazy List"
    const val DEPARTMENT_STICKY_HEADER = "Person Department Sticky - %s"

    const val PERSON_NAME = "Person Name on Collapsible Content"
    const val COLLAPSIBLE_CONTENT = "Person Collapsible Content"

    const val SHIMMERING_BIOGRAPHY_CONTENT = "Shimmering Biography Content"

    const val CREDITS_FILTER_BOTTOM_SHEET = "Person Credits Filter Bottom Sheet"
    const val LAYOUT_SWITCHER = "Person Layout Switcher"

    const val KNOWN_FOR_SECTION = "Person Known For Section"
    const val KNOWN_FOR_SECTION_LIST = "Person Known For Section Lazy List"

    const val CREDIT_MEDIA_ITEM = "Person Credit Media Item - %s"

    const val EMPTY_CONTENT_CREDIT_CARD = "Person Empty Content Credit Card - %s"

    const val ABOUT_FORM = "Person About Form"
    const val MOVIES_FORM = "Person Movies Form style grid = %s"
    const val TV_SHOWS_FORM = "Person TV Shows Form style grid = %s"
  }

  object Dialogs {
    const val ALERT_DIALOG = "Dialogs Alert Dialog"
    const val CONFIRM_BUTTON = "Dialog Confirm Button"
    const val DISMISS_BUTTON = "Dialog Dismiss Button"

    const val SELECT_SEASONS_DIALOG = "Select Seasons Dialog"
    const val TOGGLE_ALL_SEASONS_SWITCH = "Season Toggle All Switch"
    const val SEASON_SWITCH = "Season Switch %s"
    const val SEASON_ROW = "Season Row %s"
    const val REQUEST_MOVIE_DIALOG = "Request Movie Dialog"
    const val REQUEST_MOVIE_BUTTON = "Request Movie Button"
  }

  object Modal {
    const val BOTTOM_SHEET = "Modal Bottom Sheet"
  }

  object Settings {
    const val SCREEN_CONTENT = "Settings Screen Content"

    const val TOP_APP_BAR = "Settings Top App Bar"
    const val NAVIGATION_ICON = "Settings Navigation Icon"

    const val RADIO_BUTTON_SELECT_OPTION = "Settings Radio Button Select Option %s"

    object Account {
      const val LOGIN_BUTTON = "Account Login Button"
      const val LOGOUT_BUTTON = "Account Logout Button"
    }

    object About {
      const val CARD = "Application About Card"
      const val SCROLLABLE_CONTENT = "About Lazy List"
    }

    object Jellyseerr {
      const val INITIAL_CONTENT = "Jellyseerr Initial Content"
      const val LOGGED_IN_CONTENT = "Jellyseerr Logged In Content"
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
    const val SPOILERS_OBFUSCATION = "Spoilers Obfuscation Button"
  }

  object Watchlist {
    const val TAB_BAR = "Watchlist Tab Bar $%s"
    const val WATCHLIST_CONTENT = "Watchlist Content with data"
    const val WATCHLIST_SCREEN = "Watchlist Screen"
    const val WATCHLIST_ERROR_CONTENT = "Watchlist Error Content"
  }

  object Search {
    const val SEARCH_SCAFFOLD = "Search Scaffold"
  }

  object Credits {
    const val TAB_BAR = "Credits Tab Bar $%s"
    const val CAST_CREDITS_CONTENT = "Credits Content with cast"
    const val CREW_CREDITS_CONTENT = "Credits Content with crew"
  }

  object Onboarding {
    const val SCREEN = "Onboarding Screen"
    const val ONBOARDING_PAGE = "Onboarding Page %s"
  }

  object Shimmer {
    const val HALF_LINE = "Shimmer Half Line - %s"
    const val LINE = "Shimmer Line - %s"
  }
}
