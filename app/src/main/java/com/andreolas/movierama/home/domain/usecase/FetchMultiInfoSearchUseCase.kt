package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.search.multi.MultiSearchRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

data class MultiSearchResult(
  val query: String,
  val searchList: List<MediaItem>,
)

open class FetchMultiInfoSearchUseCase @Inject constructor(
  private val moviesRepository: MoviesRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<MultiSearchRequestApi, MultiSearchResult>(dispatcher) {
  override fun execute(
    parameters: MultiSearchRequestApi,
  ): Flow<Result<MultiSearchResult>> {
    val favoriteMovies = moviesRepository.fetchFavoriteIds()
    val searchResult = moviesRepository.fetchMultiInfo(parameters)

    return favoriteMovies
      .distinctUntilChanged()
      .combineTransform(searchResult) { favorite, search ->
        if (search is Result.Loading) {
          emit(Result.Loading)
        }
        if (favorite is Result.Success && search is Result.Success) {
          val searchWithFavorites = getMediaWithUpdatedFavoriteStatus(
            Result.Success(favorite.data),
            Result.Success(search.data)
          )
          emit(
            Result.Success(
              MultiSearchResult(
                query = parameters.query,
                searchList = searchWithFavorites,
              )
            )
          )
        } else if (search is Result.Error) {
          emit(Result.Error(Exception("Something went wrong.")))
        }
      }.conflate()
  }
}

fun getMediaWithUpdatedFavoriteStatus(
  favoriteIds: Result.Success<List<Int>>,
  mediaResult: Result.Success<List<MediaItem>>,
): List<MediaItem> {
  return mediaResult.data.map { searchItem ->
    when (searchItem) {
      is MediaItem.Media.Movie -> favoriteIds.data.find { id -> id == searchItem.id }?.let {
        searchItem.copy(isFavorite = true)
      } ?: searchItem
      is MediaItem.Media.TV -> favoriteIds.data.find { id -> id == searchItem.id }?.let {
        searchItem.copy(isFavorite = true)
      } ?: searchItem
      is MediaItem.Person -> searchItem
      MediaItem.Unknown -> searchItem
    }
  }
}
