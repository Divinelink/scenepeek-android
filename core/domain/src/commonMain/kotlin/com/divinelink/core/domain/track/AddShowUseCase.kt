package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.flow.collect

class AddShowUseCase(
  private val repository: MediaRepository,
  dispatcher: DispatcherProvider,
) : UseCase<MediaItem.Media, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: MediaItem.Media) {
    val showId = parameters.id

    val currentStatus = repository.getUserShowStatus(showId)

    if (currentStatus != null) {
      repository.deleteShow(showId)
    } else {
      repository.saveMediaItem(parameters)

      repository.addShow(showId)

      var targetSeason = repository.findFirstIncompleteSeason(showId)
      if (targetSeason == null) {
        val localMax = repository.getMaxLocalSeason(showId) ?: 0

        if (localMax > 0) {
          // Check if we skipped a season (e.g., have S1 & S3, but not S2)
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
