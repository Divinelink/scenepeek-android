package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@Deprecated("Use MarkAsFavoriteUseCase instead")
open class RemoveFavoriteUseCase @Inject constructor(
  private val repository: MediaRepository,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Int, Unit>(dispatcher) {
  override suspend fun execute(parameters: Int) {
    val result = repository.removeFavoriteMedia(parameters, MediaType.MOVIE)

    return result.data
  }
}
