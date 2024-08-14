package com.divinelink.feature.details.media.usecase

import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.MediaDetailsParams
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.commons.domain.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class FetchAccountMediaDetailsUseCase(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<MediaDetailsParams, AccountMediaDetails>(dispatcher.io) {
  override fun execute(parameters: MediaDetailsParams): Flow<Result<AccountMediaDetails>> = flow {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      emit(Result.failure(SessionException.Unauthenticated()))
      return@flow
    } else {
      val request = when (parameters.mediaType) {
        MediaType.MOVIE -> AccountMediaDetailsRequestApi.Movie(
          movieId = parameters.id,
          sessionId = sessionId,
        )
        MediaType.TV -> AccountMediaDetailsRequestApi.TV(
          seriesId = parameters.id,
          sessionId = sessionId,
        )
        else -> throw IllegalArgumentException("Unsupported media type: ${parameters.mediaType}")
      }

      repository.fetchAccountMediaDetails(
        request = request,
      ).collect {
        emit(Result.success(it.data))
      }
    }
  }
}
