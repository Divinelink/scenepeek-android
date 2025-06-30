package com.divinelink.feature.details.media.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

data class AddToWatchlistParameters(
  val id: Int,
  val mediaType: MediaType,
  val addToWatchlist: Boolean,
)

open class AddToWatchlistUseCase(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<AddToWatchlistParameters, Unit>(dispatcher.io) {
  override fun execute(parameters: AddToWatchlistParameters): Flow<Result<Unit>> = flow {
    val accountId = sessionStorage.accountId
    val sessionId = sessionStorage.sessionId

    if (accountId == null || sessionId == null) {
      // TODO Should return Unauthenticated exception
      emit(Result.failure(SessionException.InvalidAccountId()))
      return@flow
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

      val response = repository.addToWatchlist(request).last()

      emit(Result.success(response.data))
    }
  }
}
