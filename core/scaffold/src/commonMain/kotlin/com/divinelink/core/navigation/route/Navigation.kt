package com.divinelink.core.navigation.route

import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.model.user.data.UserDataSection
import kotlinx.serialization.Serializable

sealed interface Navigation {
  @Serializable
  data object Back : Navigation

  @Serializable
  data object HomeRoute : Navigation

  @Serializable
  data class SearchRoute(val entryPoint: SearchEntryPoint) : Navigation

  @Serializable
  data object SettingsRoute : Navigation

  @Serializable
  data object AboutSettingsRoute : Navigation

  @Serializable
  data class JellyseerrSettingsRoute(val withNavigationBar: Boolean) : Navigation

  @Serializable
  data object AppearanceSettingsRoute : Navigation

  @Serializable
  data object DetailsPreferencesSettingsRoute : Navigation

  @Serializable
  data object AccountSettingsRoute : Navigation

  @Serializable
  data object LinkHandlingSettingsRoute : Navigation

  @Serializable
  sealed interface Onboarding : Navigation {
    @Serializable
    data object ModalRoute : Onboarding

    @Serializable
    data object FullScreenRoute : Onboarding
  }

  @Serializable
  data class PersonRoute(
    val id: Long,
    val knownForDepartment: String?,
    val name: String?,
    val profilePath: String?,
    val gender: Int,
  ) : Navigation

  @Serializable
  data class CreditsRoute(
    val id: Long,
    val mediaType: String?,
  ) : Navigation

  @Serializable
  data class DetailsRoute(
    val id: Int,
    val mediaType: String,
    val isFavorite: Boolean?,
  ) : Navigation

  @Serializable
  data object ProfileRoute : Navigation

  @Serializable
  data object TMDBAuthRoute : Navigation

  @ConsistentCopyVisibility
  @Serializable
  data class UserDataRoute private constructor(val section: String) : Navigation {
    constructor(
      section: UserDataSection,
    ) : this(
      section = section.value,
    )
  }

  @Serializable
  data class AddToListRoute(
    val id: Int,
    val mediaType: String,
  ) : Navigation

  @Serializable
  data class EditListRoute(
    val id: Int,
    val name: String,
    val backdropPath: String?,
    val description: String,
    val public: Boolean,
  ) : Navigation

  @Serializable
  data object CreateListRoute : Navigation

  @Serializable
  data object ListsRoute : Navigation

  @Serializable
  data class ListDetailsRoute(
    val id: Int,
    val name: String,
    val backdropPath: String?,
    val description: String,
    val public: Boolean,
  ) : Navigation

  @Serializable
  data class WebViewRoute(
    val url: String,
    val title: String,
  ) : Navigation

  @Serializable
  sealed interface ActionMenuRoute : Navigation {

    @Serializable
    data class Media(val encodedMediaItem: String) : ActionMenuRoute
  }

  @Serializable
  data object JellyseerrRequestsRoute : Navigation

  @Serializable
  data object DiscoverRoute : Navigation

  @Serializable
  data class MediaPosterRoute(
    val posterPath: String,
  ) : Navigation

  @Serializable
  data class MediaListsRoute(val section: MediaListSection) : Navigation
}
