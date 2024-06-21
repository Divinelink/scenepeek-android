package com.andreolas.movierama.home.domain.usecase

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

data class SearchResult(
  val query: String,
  val searchList: List<MediaItem.Media>,
)

open class GetSearchMoviesUseCase @Inject constructor(
  private val repository: MediaRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<SearchRequestApi, SearchResult>(dispatcher) {
  override fun execute(parameters: SearchRequestApi): Flow<Result<SearchResult>> {
    val favoriteMovies = repository.fetchFavoriteIds()
    val searchMovies = repository.fetchSearchMovies(parameters)

    return favoriteMovies
      .distinctUntilChanged()
      .combineTransform(searchMovies) { favorite, search ->
//        if (search is Result.Loading) {
//          emit(Result.Loading)
//        }
        if (favorite.isSuccess && search.isSuccess) {
          val searchWithFavorites = getMediaWithUpdatedFavoriteStatus(
            Result.success(favorite.data).data,
            Result.success(search.data).data,
          )
          emit(
            Result.success(
              SearchResult(
                query = parameters.query,
                searchList = searchWithFavorites.filterIsInstance<MediaItem.Media>(),
              ),
            ),
          )
        } else if (search.isFailure) {
          emit(Result.failure(Exception("Something went wrong.")))
        }
      }.conflate()
  }
}
