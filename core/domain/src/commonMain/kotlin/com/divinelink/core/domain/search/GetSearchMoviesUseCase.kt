package com.divinelink.core.domain.search

import com.divinelink.core.commons.data
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class SearchResult(
  val query: String,
  val searchList: List<MediaItem.Media>,
)

open class GetSearchMoviesUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<SearchRequestApi, SearchResult>(dispatcher.default) {
  override fun execute(parameters: SearchRequestApi): Flow<Result<SearchResult>> =
    repository.fetchSearchMovies(parameters).map {
      Result.success(
        SearchResult(
          query = parameters.query,
          searchList = it.data,
        ),
      )
    }
}
