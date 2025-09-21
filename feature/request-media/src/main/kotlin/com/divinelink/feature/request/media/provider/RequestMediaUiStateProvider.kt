package com.divinelink.feature.request.media.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.details.season.SeasonFactory
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
      isEditMode = false,
      seasons = SeasonFactory.all(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestMediaUiState.initial(
      isEditMode = false,
      seasons = SeasonFactory.allWithStatus(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestMediaUiState.initial(
      isEditMode = false,
      seasons = SeasonFactory.partiallyAvailable(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestMediaUiState.initial(
      isEditMode = false,
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      selectedInstance = LCEState.Content(SonarrInstanceDetailsFactory.sonarr.server),
    ),
    RequestMediaUiState.initial(
      isEditMode = false,
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
      isEditMode = false,
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
      isEditMode = false,
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
      isEditMode = false,
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
      isEditMode = false,
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
      isEditMode = true,
      seasons = emptyList(),
      media = MediaItemFactory.FightClub(),
    ).copy(
      instances = RadarrInstanceFactory.all,
      profiles = InstanceProfileFactory.movie,
      selectedInstance = LCEState.Content(RadarrInstanceFactory.radarr),
      selectedProfile = LCEState.Content(InstanceProfileFactory.sd.copy(isDefault = true)),
      selectedRootFolder = LCEState.Content(InstanceRootFolderFactory.tv.copy(isDefault = false)),
    ),
  )
}
