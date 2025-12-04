package com.divinelink.scenepeek.home.navigation

import androidx.navigation.NavController
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.navigateToAddToList
import com.divinelink.core.navigation.route.navigateToCreateList
import com.divinelink.core.navigation.route.navigateToDetails
import com.divinelink.core.navigation.route.navigateToDiscover
import com.divinelink.core.navigation.route.navigateToEditList
import com.divinelink.core.navigation.route.navigateToListDetails
import com.divinelink.core.navigation.route.navigateToLists
import com.divinelink.core.navigation.route.navigateToOnboarding
import com.divinelink.core.navigation.route.navigateToPerson
import com.divinelink.core.navigation.route.navigateToPoster
import com.divinelink.core.navigation.route.navigateToRequests
import com.divinelink.core.navigation.route.navigateToSearchFromHome
import com.divinelink.core.navigation.route.navigateToSearchFromTab
import com.divinelink.core.navigation.route.navigateToTMDBAuth
import com.divinelink.core.navigation.route.navigateToUserData
import com.divinelink.core.navigation.route.navigateToWebView
import com.divinelink.core.navigation.route.openDefaultActionMenuModal
import com.divinelink.feature.credits.navigation.navigateToCredits
import com.divinelink.feature.settings.navigation.about.navigateToAboutSettings
import com.divinelink.feature.settings.navigation.account.navigateToAccountSettings
import com.divinelink.feature.settings.navigation.account.navigateToJellyseerrSettings
import com.divinelink.feature.settings.navigation.appearance.navigateToAppearanceSettings
import com.divinelink.feature.settings.navigation.details.navigateToDetailsPreferenceSettings
import com.divinelink.feature.settings.navigation.links.navigateToLinkHandlingSettings
import com.divinelink.feature.settings.navigation.settings.navigateToSettings

fun NavController.findNavigation(route: Navigation) {
  when (route) {
    Navigation.Back -> navigateUp()
    Navigation.AboutSettingsRoute -> navigateToAboutSettings()
    Navigation.AccountSettingsRoute -> navigateToAccountSettings()
    Navigation.DetailsPreferencesSettingsRoute -> navigateToDetailsPreferenceSettings()
    Navigation.LinkHandlingSettingsRoute -> navigateToLinkHandlingSettings()
    Navigation.ListsRoute -> navigateToLists()
    Navigation.Onboarding.ModalRoute -> navigateToOnboarding(fullscreen = false)
    Navigation.Onboarding.FullScreenRoute -> navigateToOnboarding(fullscreen = true)
    Navigation.SettingsRoute -> navigateToSettings()
    Navigation.TMDBAuthRoute -> navigateToTMDBAuth()
    Navigation.CreateListRoute -> navigateToCreateList()
    Navigation.AppearanceSettingsRoute -> navigateToAppearanceSettings()
    is Navigation.AddToListRoute -> navigateToAddToList(route)
    is Navigation.CreditsRoute -> navigateToCredits(route)
    is Navigation.DetailsRoute -> navigateToDetails(route)
    is Navigation.EditListRoute -> navigateToEditList(route)
    is Navigation.JellyseerrSettingsRoute -> navigateToJellyseerrSettings(route.withNavigationBar)
    is Navigation.ListDetailsRoute -> navigateToListDetails(route)
    is Navigation.PersonRoute -> navigateToPerson(route)
    is Navigation.SearchRoute -> when (route.entryPoint) {
      SearchEntryPoint.HOME -> navigateToSearchFromHome()
      SearchEntryPoint.SEARCH_TAB -> navigateToSearchFromTab()
    }
    is Navigation.UserDataRoute -> navigateToUserData(route)
    is Navigation.WebViewRoute -> navigateToWebView(route)
    is Navigation.ActionMenuRoute.Media -> openDefaultActionMenuModal(route)
    Navigation.JellyseerrRequestsRoute -> navigateToRequests()
    Navigation.DiscoverRoute -> navigateToDiscover()
    is Navigation.MediaPosterRoute -> navigateToPoster(route)

    // This is from top level navigation
    Navigation.HomeRoute -> Unit
    Navigation.ProfileRoute -> Unit
  }
}
