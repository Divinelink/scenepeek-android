package com.divinelink.core.domain.search

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.SearchTab
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class MultiSearchResult(
  val query: String,
  val tab: SearchTab,
  val searchList: List<MediaItem>,
  val page: Int,
  val canFetchMore: Boolean,
)

data class MultiSearchParameters(
  val query: String,
  val page: Int,
  val tab: SearchTab,
)

class FetchMultiInfoSearchUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<MultiSearchParameters, MultiSearchResult>(dispatcher.default) {
  override fun execute(parameters: MultiSearchParameters): Flow<Result<MultiSearchResult>> {
    val searchJob = when (parameters.tab) {
      SearchTab.All -> repository.fetchMultiInfo(
        MultiSearchRequestApi(
          query = parameters.query,
          page = parameters.page,
        ),
      )
      SearchTab.Movie -> repository.fetchSearchMovies(
        mediaType = MediaType.MOVIE,
        request = SearchRequestApi(
          query = parameters.query,
          page = parameters.page,
        ),
      )
      SearchTab.People -> repository.fetchSearchMovies(
        mediaType = MediaType.PERSON,
        request = SearchRequestApi(
          query = parameters.query,
          page = parameters.page,
        ),
      )
      SearchTab.TV -> repository.fetchSearchMovies(
        mediaType = MediaType.TV,
        request = SearchRequestApi(
          query = parameters.query,
          page = parameters.page,
        ),
      )
    }

    return searchJob.map { result ->
      result.fold(
        onSuccess = {
          Result.success(
            MultiSearchResult(
              query = parameters.query,
              tab = parameters.tab,
              page = it.page,
              searchList = it.searchList,
              canFetchMore = it.page < it.totalPages,
            ),
          )
        },
        onFailure = { Result.failure(it) },
      )
    }
  }
}
