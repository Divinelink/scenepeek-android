package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.UseCase
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class MarkAsFavoriteUseCase @Inject constructor(
  private val repository: MoviesRepository,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<MediaItem.Media, Unit>(dispatcher) {
  override suspend fun execute(parameters: MediaItem.Media) {
    val isFavorite = repository.checkIfMediaIsFavorite(
      id = parameters.id,
      mediaType = parameters.mediaType
    ).data == true

    val result = if (isFavorite) {
      repository.removeFavoriteMedia(
        id = parameters.id,
        mediaType = parameters.mediaType
      )
    } else {
      repository.insertFavoriteMedia(parameters)
    }

    when (result) {
      is Result.Success -> result.data
      is Result.Error -> throw result.exception
      Result.Loading -> throw IllegalStateException()
    }
  }
}
