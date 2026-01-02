package com.divinelink.core.domain.search

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.tab.SearchTab
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class MultiSearchResult(
  val query: String,
  val tab: SearchTab,
  val searchList: List<MediaItem>,
  val page: Int,
  val totalPages: Int,
  val canFetchMore: Boolean,
)

data class MultiSearchParameters(
  val query: String,
  val page: Int,
  val tab: SearchTab,
)

open class FetchMultiInfoSearchUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<MultiSearchParameters, MultiSearchResult>(dispatcher.default) {
  override fun execute(parameters: MultiSearchParameters): Flow<Result<MultiSearchResult>> =
    repository.fetchMultiInfo(
      MultiSearchRequestApi(
        query = parameters.query,
        page = parameters.page,
      ),
    ).map { result ->
      result.fold(
        onSuccess = {
          Result.success(
            MultiSearchResult(
              query = parameters.query,
              tab = parameters.tab,
              page = it.page,
              searchList = it.searchList,
              totalPages = it.totalPages,
              canFetchMore = it.page < it.totalPages,
            ),
          )
        },
        onFailure = { Result.failure(it) },
      )
    }
}
