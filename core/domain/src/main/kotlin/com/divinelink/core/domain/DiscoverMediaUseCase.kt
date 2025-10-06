package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.discover.DiscoverParameters
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DiscoverMediaUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<DiscoverParameters, UserDataResponse>(dispatcher.default) {

  override fun execute(parameters: DiscoverParameters): Flow<Result<UserDataResponse>> = flow {
    if (parameters.mediaType == MediaType.TV) {
      repository.discoverTvShows(
        page = parameters.page,
        filters = parameters.filters,
      ).collect { result ->
        result.fold(
          onSuccess = { emit(Result.success(it)) },
          onFailure = { emit(Result.failure(it)) },
        )
      }
    } else {
      repository.discoverMovies(
        page = parameters.page,
        filters = parameters.filters,
      ).collect { result ->
        result.fold(
          onSuccess = { emit(Result.success(it)) },
          onFailure = { emit(Result.failure(it)) },
        )
      }
    }
  }
}
