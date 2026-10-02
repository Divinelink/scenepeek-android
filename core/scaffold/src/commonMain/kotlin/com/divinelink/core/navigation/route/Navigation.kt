package com.divinelink.core.navigation.route

import androidx.navigation3.runtime.NavKey
import com.divinelink.core.model.awards.Ceremony
import com.divinelink.core.model.awards.CeremonyCategory
import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.model.track.UserShowStatus
import com.divinelink.core.model.user.data.UserDataSection
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid
import com.divinelink.core.model.details.Episode as DetailsEpisode

sealed interface Navigation : NavKey {
  @Serializable
  data object Back : Navigation
  data object TwiceBack : Navigation

  @Serializable
  data object HomeRoute : Navigation

  @Serializable
  data class SearchRoute(val entryPoint: SearchEntryPoint) : Navigation

  @Serializable
  data object SettingsRoute : Navigation

  @Serializable
  data object CustomizeDashboardRoute : Navigation

  @Serializable
  data object AboutSettingsRoute : Navigation

  @Serializable
  data object AppUpdatesSettingsRoute : Navigation

  @Serializable
  data object BackupAndRestoreSettingsRoute : Navigation

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

    @Serializable
    data object Changelog : Onboarding
  }

  @Serializable
  data class PersonRoute(
    val id: Long,
    val knownForDepartment: String?,
    val name: String?,
    val profilePath: String?,
    val gender: Int,
    val saved: Boolean?,
  ) : Navigation

  @Serializable
  data class CreditsRoute(
    val id: Long,
    val mediaType: String?,
  ) : Navigation

  @Serializable
  data class DetailsRoute(
    val id: Long,
    val mediaType: String,
    val isFavorite: Boolean? = null,
  ) : Navigation

  @Serializable
  data class SeasonRoute(
    val showId: Long,
    val seasonNumber: Int,
    val backdropPath: String?,
    val title: String,
  ) : Navigation

  @Serializable
  sealed interface Episode : Navigation {
    val showId: Long
    val seasonNumber: Int
    val showTitle: String

    @Serializable
    data class FullScreenRoute(
      override val showId: Long,
      override val showTitle: String,
      override val seasonNumber: Int,
      val seasonTitle: String,
      val episodeIndex: Int,
    ) : Episode

    @Serializable
    data class ModalRoute(
      override val showId: Long,
      override val seasonNumber: Int,
      override val showTitle: String,
      val episode: DetailsEpisode,
    ) : Episode
  }

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
    val id: Long,
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

    @Serializable
    data class ShowProgress(
      val showId: Long,
      val status: UserShowStatus,
    ) : ActionMenuRoute
  }

  @Serializable
  data class SeerrRequestsRoute(val filterValue: String) : Navigation

  @Serializable
  sealed interface Discover : Navigation {
    val mediaType: MediaType?
    val entryPointUuid: String

    @Serializable
    data class Default(
      override val mediaType: MediaType,
      override val entryPointUuid: String = Uuid.random().toHexString(),
      val encodedGenre: String? = null,
      val encodedKeyword: String? = null,
      val encodedCompany: String? = null,
    ) : Discover

    @Serializable
    data class Custom(
      override val mediaType: MediaType,
      override val entryPointUuid: String = entryPoint.value + mediaType.value,
      val entryPoint: DiscoverType,
    ) : Discover
  }

  @Serializable
  data class MediaPosterRoute(
    val posterPath: String,
  ) : Navigation

  @Serializable
  data class MediaListsRoute(val section: MediaListSection) : Navigation

  @Serializable
  data object NextUpFullscreenRoute : Navigation

  @Serializable
  data class CollectionRoute(
    val id: Int,
    val name: String,
    val backdropPath: String?,
    val posterPath: String?,
  ) : Navigation

  @Serializable
  data object UpdaterRoute : Navigation

  @Serializable
  data object AwardsRoute : Navigation

  @Serializable
  data class AwardDetailsRoute(
    val ceremony: Ceremony,
  ) : Navigation

  @Serializable
  data class AwardCategoryRoute(
    val category: CeremonyCategory,
    val ceremonyId: String,
  ) : Navigation
}
