package com.divinelink.core.domain.details.media

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.details.AccountDataSection
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.details.watchlist.AddToAccountRequest

data class AddToWatchlistParameters(
  val id: Int,
  val mediaType: MediaType,
  val section: AccountDataSection,
  val shouldAdd: Boolean,
)

open class AddToAccountUseCase(
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
      val watchlist = if (parameters.section == AccountDataSection.Watchlist) {
        parameters.shouldAdd
      } else {
        null
      }
      val favorite = if (parameters.section == AccountDataSection.Favorite) {
        parameters.shouldAdd
      } else {
        null
      }

      val request = when (parameters.mediaType) {
        MediaType.MOVIE -> AddToAccountRequest.Movie(
          movieId = parameters.id,
          accountId = accountId,
          watchlist = watchlist,
          favorite = favorite,
          sessionId = sessionId,
        )
        MediaType.TV -> AddToAccountRequest.TV(
          seriesId = parameters.id,
          accountId = accountId,
          watchlist = watchlist,
          favorite = favorite,
          sessionId = sessionId,
        )

        else -> throw IllegalArgumentException("Unsupported media type: ${parameters.mediaType}")
      }

      Result.success(repository.addToAccount(request, parameters.section).getOrThrow())
    }
  }
}
