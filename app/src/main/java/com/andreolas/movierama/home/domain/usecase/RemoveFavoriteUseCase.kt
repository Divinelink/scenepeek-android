package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaType
import gr.divinelink.core.util.domain.UseCase
import gr.divinelink.core.util.domain.data
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
