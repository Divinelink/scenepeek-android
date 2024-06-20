package com.divinelink.core.domain

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.watchlist.WatchlistParameters
import com.divinelink.core.model.watchlist.WatchlistResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class FetchWatchlistUseCase @Inject constructor(
  @IoDispatcher val dispatcher: CoroutineDispatcher,
  private val sessionStorage: SessionStorage,
  private val accountRepository: AccountRepository,
) : FlowUseCase<WatchlistParameters, WatchlistResponse>(dispatcher) {

  override fun execute(parameters: WatchlistParameters): Flow<Result<WatchlistResponse>> = flow {
    val accountId = sessionStorage.accountId.first()
    val sessionId = sessionStorage.sessionId

    if (accountId == null || sessionId == null) {
      emit(Result.failure(SessionException.Unauthenticated()))
      return@flow
    }

    if (parameters.mediaType == MediaType.TV) {
      accountRepository.fetchTvShowsWatchlist(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
        sessionId = sessionId,
      ).last().fold(
        onSuccess = {
          val canFetchMore = parameters.page < it.totalPages

          emit(
            Result.success(
              WatchlistResponse(
                data = it.list,
                totalResults = it.totalResults,
                type = MediaType.TV,
                canFetchMore = canFetchMore,
              ),
            ),
          )
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
    } else {
      accountRepository.fetchMoviesWatchlist(
        page = parameters.page,
        sortBy = parameters.sortBy.value,
        accountId = accountId,
        sessionId = sessionId,
      ).last().fold(
        onSuccess = {
          val canFetchMore = parameters.page < it.totalPages

          emit(
            Result.success(
              WatchlistResponse(
                data = it.list,
                totalResults = it.totalResults,
                type = MediaType.MOVIE,
                canFetchMore = canFetchMore,
              ),
            ),
          )
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
    }
  }
}
