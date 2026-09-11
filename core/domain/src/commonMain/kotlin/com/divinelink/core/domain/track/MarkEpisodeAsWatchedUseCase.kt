package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.details.Episode

class MarkEpisodeAsWatchedUseCase(
  private val repository: MediaRepository,
  private val fetchNextMissingSeasonUseCase: FetchNextMissingSeasonUseCase,
  dispatcher: DispatcherProvider,
) : UseCase<Episode, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: Episode) {
    repository.markEpisodeAsWatched(episodeId = parameters.id.toLong())

    fetchNextMissingSeasonUseCase(parameters.showId)
  }
}
