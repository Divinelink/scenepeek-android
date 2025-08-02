package com.divinelink.core.navigation.route

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
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
    val gender: Gender?,
  ) : Navigation

  @Serializable
  data class CreditsRoute(
    val id: Long,
    val mediaType: MediaType?,
  ) : Navigation

  @Serializable
  data class DetailsRoute(
    val id: Int,
    val mediaType: MediaType,
    val isFavorite: Boolean?,
  ) : Navigation

  @Serializable
  data object ProfileRoute : Navigation

  @Serializable
  data object TMDBAuthRoute : Navigation

  @Serializable
  data class UserDataRoute(val userDataSection: UserDataSection) : Navigation

  @Serializable
  data class AddToListRoute(
    val id: Int,
    val mediaType: MediaType,
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
}
