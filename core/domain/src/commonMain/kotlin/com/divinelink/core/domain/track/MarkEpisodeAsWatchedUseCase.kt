package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.model.details.Episode
import kotlinx.datetime.LocalDate

data class MarkEpisodeParameters(
  val episode: Episode,
  val watchedAt: LocalDate,
)

class MarkEpisodeAsWatchedUseCase(
  private val repository: TrackRepository,
  private val fetchNextMissingSeasonUseCase: FetchNextMissingSeasonUseCase,
  dispatcher: DispatcherProvider,
) : UseCase<MarkEpisodeParameters, Boolean>(dispatcher.default) {
  override suspend fun execute(parameters: MarkEpisodeParameters): Boolean {
    val episodeId = parameters.episode.id
    val showId = parameters.episode.showId

    val isWatched = repository.isWatchedEpisode(episodeId.toLong())

    if (isWatched) {
      repository.unmarkEpisodeAsWatched(episodeId = episodeId.toLong())
    } else {
      repository.markEpisodeAsWatched(
        episodeId = episodeId.toLong(),
        watchedAt = parameters.watchedAt.toString(),
      )
    }

    fetchNextMissingSeasonUseCase(showId)

    return !isWatched
  }
}
