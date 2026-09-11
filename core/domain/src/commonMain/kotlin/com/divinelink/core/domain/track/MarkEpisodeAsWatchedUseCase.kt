package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.model.details.Episode

class MarkEpisodeAsWatchedUseCase(
  private val repository: TrackRepository,
  private val fetchNextMissingSeasonUseCase: FetchNextMissingSeasonUseCase,
  dispatcher: DispatcherProvider,
) : UseCase<Episode, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: Episode) {
    val isWatched = repository.isWatchedEpisode(parameters.id.toLong())

    if (isWatched) {
      repository.unmarkEpisodeAsWatched(episodeId = parameters.id.toLong())
    } else {
      repository.markEpisodeAsWatched(episodeId = parameters.id.toLong())
    }

    fetchNextMissingSeasonUseCase(parameters.showId)
  }
}
