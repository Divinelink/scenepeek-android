package com.divinelink.feature.request.media.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequestFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceRootFolderFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.feature.request.media.LCEState
import com.divinelink.feature.request.media.RequestMediaUiState

@ExcludeFromKoverReport
class RequestMediaUiStateProvider : PreviewParameterProvider<RequestMediaUiState?> {
  override val values: Sequence<RequestMediaUiState?> = sequenceOf(
    RequestMediaUiState.initial(
      request = null,
      seasons = SeasonFactory.all(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = SeasonFactory.allWithStatus(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = SeasonFactory.partiallyAvailable(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      selectedInstance = LCEState.Content(SonarrInstanceDetailsFactory.sonarr.server),
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      profiles = InstanceProfileFactory.tv,
      selectedInstance = LCEState.Content(SonarrInstanceDetailsFactory.sonarr.server),
      selectedProfile = LCEState.Content(InstanceProfileFactory.hd7201080),
      selectedRootFolder = LCEState.Content(InstanceRootFolderFactory.tv),
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      profiles = InstanceProfileFactory.tv,
      selectedInstance = LCEState.Idle,
      selectedProfile = LCEState.Content(InstanceProfileFactory.hd7201080),
      selectedRootFolder = LCEState.Idle,
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      profiles = InstanceProfileFactory.tv,
      selectedInstance = LCEState.Idle,
      selectedProfile = LCEState.Idle,
      selectedRootFolder = LCEState.Idle,
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      profiles = InstanceProfileFactory.tv,
      selectedInstance = LCEState.Content(SonarrInstanceFactory.anime),
      selectedProfile = LCEState.Content(InstanceProfileFactory.sd.copy(isDefault = true)),
      selectedRootFolder = LCEState.Content(InstanceRootFolderFactory.tv.copy(isDefault = false)),
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = emptyList(),
      media = MediaItemFactory.FightClub(),
    ).copy(
      instances = RadarrInstanceFactory.all,
      profiles = InstanceProfileFactory.movie,
      selectedInstance = LCEState.Content(RadarrInstanceFactory.radarr),
      selectedProfile = LCEState.Content(InstanceProfileFactory.sd.copy(isDefault = true)),
      selectedRootFolder = LCEState.Content(InstanceRootFolderFactory.tv.copy(isDefault = false)),
    ),
    RequestMediaUiState.initial(
      request = null,
      seasons = emptyList(),
      media = MediaItemFactory.FightClub(),
    ).copy(
      request = JellyseerrRequestFactory.movie(),
      instances = RadarrInstanceFactory.all,
      profiles = InstanceProfileFactory.movie,
      selectedInstance = LCEState.Content(RadarrInstanceFactory.radarr),
      selectedProfile = LCEState.Content(InstanceProfileFactory.sd.copy(isDefault = true)),
      selectedRootFolder = LCEState.Content(InstanceRootFolderFactory.tv.copy(isDefault = false)),
    ),
  )
}
