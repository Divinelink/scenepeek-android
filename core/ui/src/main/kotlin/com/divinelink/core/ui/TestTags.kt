package com.divinelink.core.ui

object TestTags {

  const val LOADING_CONTENT_TAG = "LOADING_CONTENT_TAG"
  const val MOVIES_LIST_TAG = "MOVIES_LIST_TAG"
  const val SCROLL_TO_TOP_BUTTON = "SCROLL_TO_TOP_BUTTON_TAG"

  object Details {
    const val YOUR_RATING = "Details Your Rating"
    const val RATE_DIALOG = "Details Rate Dialog"
    const val RATE_SLIDER = "Details Rate Slider"
  }

  object Dialogs {
    const val ALERT_DIALOG = "Dialogs Alert Dialog"
    const val CONFIRM_BUTTON = "Dialog Confirm Button"
    const val DISMISS_BUTTON = "Dialog Dismiss Button"
  }

  object Settings {
    const val TOP_APP_BAR = "Settings Top App Bar"
    const val NAVIGATION_ICON = "Settings Navigation Icon"

    object Account {
      const val LOGIN_BUTTON = "Account Login Button"
      const val LOGOUT_BUTTON = "Account Logout Button"
    }
  }

  object Menu {
    const val MENU_BUTTON_VERTICAL = "Menu Button Vertical"
    const val DROPDOWN_MENU = "Dropdown Menu"
    const val MENU_ITEM = "Menu Item %s"
  }

  object Watchlist {
    const val TAB_BAR = "Watchlist Tab Bar $%s"
    const val WATCHLIST_CONTENT = "Watchlist Content with data"
    const val WATCHLIST_SCREEN = "Watchlist Screen"
    const val WATCHLIST_ERROR_CONTENT = "Watchlist Error Content"
  }
}
