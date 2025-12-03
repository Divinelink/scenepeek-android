package com.divinelink.core.domain.search

import com.divinelink.core.commons.data
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class MultiSearchResult(
  val query: String,
  val searchList: List<MediaItem>,
  val totalPages: Int,
)

data class MultiSearchParameters(
  val query: String,
  val page: Int,
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
      Result.success(
        MultiSearchResult(
          query = parameters.query,
          searchList = result.data.searchList,
          totalPages = result.data.totalPages,
        ),
      )
    }
}
