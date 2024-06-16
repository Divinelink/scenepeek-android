package com.divinelink.core.domain

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.WatchlistSorting
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

data class WatchlistParameters(
  val page: Int,
  val sortBy: WatchlistSorting = WatchlistSorting.DESCENDING,
  val mediaType: MediaType = MediaType.TV
)

data class WatchlistResponse(
  val data: List<MediaItem.Media>,
  val type: MediaType
)

class FetchWatchlistUseCase @Inject constructor(
  @IoDispatcher val dispatcher: CoroutineDispatcher,
  private val sessionStorage: SessionStorage,
  private val accountRepository: AccountRepository
) : FlowUseCase<WatchlistParameters, WatchlistResponse>(dispatcher) {

  override fun execute(parameters: WatchlistParameters): Flow<Result<WatchlistResponse>> = flow {
    val accountId = sessionStorage.accountId.first()

    if (accountId == null) {
      emit(Result.failure(SessionException.InvalidAccountId()))
      return@flow
    }

    if (parameters.mediaType == MediaType.TV) {
      accountRepository.fetchTvShowsWatchlist(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
      ).last().fold(
        onSuccess = {
          emit(Result.success(WatchlistResponse(it, MediaType.TV)))
        },
        onFailure = {
          emit(Result.failure(it))
        }
      )
    } else {
      accountRepository.fetchMoviesWatchlist(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
      ).last().fold(
        onSuccess = {
          emit(Result.success(WatchlistResponse(it, MediaType.MOVIE)))
        },
        onFailure = {
          emit(Result.failure(it))
        }
      )
    }
  }
}
