package com.divinelink.core.domain.search

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged

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
) : FlowUseCase<MultiSearchParameters, MultiSearchResult>(dispatcher.io) {
  override fun execute(parameters: MultiSearchParameters): Flow<Result<MultiSearchResult>> {
    val favoriteMovies = repository.fetchFavoriteIds()
    val searchResult = repository.fetchMultiInfo(
      MultiSearchRequestApi(
        query = parameters.query,
        page = parameters.page,
      ),
    )

    return favoriteMovies
      .distinctUntilChanged()
      .combineTransform(searchResult) { favorite, search ->
        if (favorite.isSuccess && search.isSuccess) {
          val searchWithFavorites = getMediaWithUpdatedFavoriteStatus(
            favoriteIds = Result.success(favorite.data).data,
            // Filter out persons for now
            mediaResult = Result.success(search.data.searchList).data,
          )
          emit(
            Result.success(
              MultiSearchResult(
                query = parameters.query,
                searchList = searchWithFavorites,
                totalPages = search.data.totalPages,
              ),
            ),
          )
        } else if (search.isFailure) {
          emit(Result.failure(Exception("Something went wrong.")))
        }
      }.conflate()
  }
}

fun getMediaWithUpdatedFavoriteStatus(
  favoriteIds: List<Pair<Int, MediaType>>,
  mediaResult: List<MediaItem>,
): List<MediaItem> {
  val favoriteIdSet = favoriteIds.map { it.first to it.second }.toSet()

  return mediaResult.map { mediaItem ->
    when (mediaItem) {
      is MediaItem.Media -> {
        val isFavorite = mediaItem.id to mediaItem.mediaType in favoriteIdSet
        when (mediaItem) {
          is MediaItem.Media.Movie -> mediaItem.copy(isFavorite = isFavorite)
          is MediaItem.Media.TV -> mediaItem.copy(isFavorite = isFavorite)
        }
      }
      is MediaItem.Person, MediaItem.Unknown -> mediaItem
    }
  }
}
