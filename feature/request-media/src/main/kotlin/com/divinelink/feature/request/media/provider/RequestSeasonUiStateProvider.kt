package com.divinelink.feature.request.media.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceRootFolderFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.feature.request.media.LCEState
import com.divinelink.feature.request.media.RequestSeasonsUiState

@ExcludeFromKoverReport
class RequestSeasonUiStateProvider : PreviewParameterProvider<RequestSeasonsUiState?> {
  override val values: Sequence<RequestSeasonsUiState?> = sequenceOf(
    RequestSeasonsUiState.initial(
      seasons = SeasonFactory.all(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestSeasonsUiState.initial(
      seasons = SeasonFactory.allWithStatus(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestSeasonsUiState.initial(
      seasons = SeasonFactory.partiallyAvailable(),
      media = MediaItemFactory.theOffice(),
    ),
    RequestSeasonsUiState.initial(
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      selectedInstance = LCEState.Content(SonarrInstanceDetailsFactory.sonarr.server),
    ),
    RequestSeasonsUiState.initial(
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      profiles = InstanceProfileFactory.tv,
      selectedInstance = LCEState.Content(SonarrInstanceDetailsFactory.sonarr.server),
      selectedProfile = LCEState.Content(InstanceProfileFactory.hd7201080),
      selectedRootFolder = LCEState.Content(InstanceRootFolderFactory.tv),
    ),
    RequestSeasonsUiState.initial(
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      profiles = InstanceProfileFactory.tv,
      selectedInstance = LCEState.Error,
      selectedProfile = LCEState.Content(InstanceProfileFactory.hd7201080),
      selectedRootFolder = LCEState.Error,
    ),
    RequestSeasonsUiState.initial(
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      profiles = InstanceProfileFactory.tv,
      selectedInstance = LCEState.Error,
      selectedProfile = LCEState.Error,
      selectedRootFolder = LCEState.Error,
    ),
    RequestSeasonsUiState.initial(
      seasons = listOf(SeasonFactory.season1()),
      media = MediaItemFactory.theOffice(),
    ).copy(
      instances = SonarrInstanceFactory.all,
      profiles = InstanceProfileFactory.tv,
      selectedInstance = LCEState.Content(SonarrInstanceFactory.anime),
      selectedProfile = LCEState.Content(InstanceProfileFactory.sd.copy(isDefault = true)),
      selectedRootFolder = LCEState.Content(InstanceRootFolderFactory.tv.copy(isDefault = false)),
    ),
  )
}
