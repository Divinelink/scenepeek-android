package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.first

data class CompleteSeasonParameters(
  val showId: Long,
  val seasonNumber: Int,
  val seasonComplete: Boolean,
)

class CompleteSeasonUseCase(
  private val repository: MediaRepository,
  private val fetchNextMissingSeasonUseCase: FetchNextMissingSeasonUseCase,
  dispatcher: DispatcherProvider,
) : UseCase<CompleteSeasonParameters, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: CompleteSeasonParameters) {
    val episodeIds = repository.fetchSeasonEpisodes(
      showId = parameters.showId,
      season = parameters.seasonNumber,
    )
      .first { it is Resource.Success }
      .let { (it as Resource.Success).data }
      ?.map { it.id.toLong() }
      ?: emptyList()

    if (parameters.seasonComplete) {
      repository.unmarkSeasonAsWatched(episodeIds)
    } else {
      repository.markSeasonAsWatched(episodeIds)
    }

    fetchNextMissingSeasonUseCase(parameters.showId)
  }
}
