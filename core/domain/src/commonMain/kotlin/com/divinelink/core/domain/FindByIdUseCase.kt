package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FindByIdUseCase(
  private val repository: DetailsRepository,
  dispatcher: DispatcherProvider,
) : FlowUseCase<String, MediaItem>(dispatcher.io) {

  override fun execute(parameters: String): Flow<Result<MediaItem>> = flow {
    val result = repository.findById(parameters).first()

    emit(result)
  }
}
