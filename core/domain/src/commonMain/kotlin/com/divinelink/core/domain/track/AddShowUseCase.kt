package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.details.TV

class AddShowUseCase(
  private val repository: MediaRepository,
  private val fetchNextMissingSeasonUseCase: FetchNextMissingSeasonUseCase,
  dispatcher: DispatcherProvider,
) : UseCase<TV, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: TV) {
    val showId = parameters.id

    val currentStatus = repository.getUserShowStatus(showId)

    if (currentStatus != null) {
      repository.deleteShow(showId)
    } else {
      repository.saveShowDetails(parameters)
      repository.addShow(showId = showId)

      fetchNextMissingSeasonUseCase(showId)
    }
  }
}
