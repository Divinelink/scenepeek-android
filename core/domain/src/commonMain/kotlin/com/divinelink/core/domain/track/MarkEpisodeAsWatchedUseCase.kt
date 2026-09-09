package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.model.details.Episode
import kotlinx.coroutines.flow.collect

class MarkEpisodeAsWatchedUseCase(
  private val repository: MediaRepository,
  private val trackRepository: TrackRepository,
  dispatcher: DispatcherProvider,
) : UseCase<Episode, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: Episode) {
    val showId = parameters.showId
    repository.markEpisodeAsWatched(episodeId = parameters.id.toLong())

    val isSeasonComplete = trackRepository.isSeasonComplete(
      showId = showId,
      seasonNumber = parameters.seasonNumber,
    )

    if (isSeasonComplete) {
      // Re-use same logic with AddShowUseCase that finds the next unwatched season
      // TODO Refactor this to avoid duplicates
      var targetSeason = repository.findFirstIncompleteSeason(showId)
      if (targetSeason == null) {
        val localMax = repository.getMaxLocalSeason(showId) ?: 0

        if (localMax > 0) {
          targetSeason = repository.findFirstMissingSeason(showId, localMax)

          if (targetSeason == null) {
            targetSeason = localMax + 1
          }
        } else {
          targetSeason = 1
        }
      }

      repository
        .fetchSeasonDetails(showId, targetSeason)
        .collect()
    }
  }
}
