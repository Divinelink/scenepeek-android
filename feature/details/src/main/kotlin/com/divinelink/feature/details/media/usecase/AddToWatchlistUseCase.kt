package com.divinelink.feature.details.media.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi

data class AddToWatchlistParameters(
  val id: Int,
  val mediaType: MediaType,
  val addToWatchlist: Boolean,
)

open class AddToWatchlistUseCase(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<AddToWatchlistParameters, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: AddToWatchlistParameters) {
    val accountId = sessionStorage.accountId
    val sessionId = sessionStorage.sessionId

    if (accountId == null || sessionId == null) {
      throw SessionException.Unauthenticated()
    } else {
      val request = when (parameters.mediaType) {
        MediaType.MOVIE -> AddToWatchlistRequestApi.Movie(
          movieId = parameters.id,
          accountId = accountId,
          addToWatchlist = parameters.addToWatchlist,
          sessionId = sessionId,
        )
        MediaType.TV -> AddToWatchlistRequestApi.TV(
          seriesId = parameters.id,
          accountId = accountId,
          addToWatchlist = parameters.addToWatchlist,
          sessionId = sessionId,
        )

        else -> throw IllegalArgumentException("Unsupported media type: ${parameters.mediaType}")
      }

      Result.success(repository.addToWatchlist(request).getOrThrow())
    }
  }
}
