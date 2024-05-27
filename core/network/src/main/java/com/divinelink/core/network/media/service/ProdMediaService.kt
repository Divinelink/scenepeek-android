package com.divinelink.core.network.media.service

import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarResponseApi
import com.divinelink.core.network.media.model.details.videos.VideosRequestApi
import com.divinelink.core.network.media.model.details.videos.VideosResponseApi
import com.divinelink.core.network.media.model.popular.PopularRequestApi
import com.divinelink.core.network.media.model.popular.PopularResponseApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestBodyApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchResponseApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdMediaService @Inject constructor(
  private val restClient: RestClient,
) : MediaService {

  override fun fetchPopularMovies(request: PopularRequestApi): Flow<PopularResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/movie/popular?"
    val url = baseUrl + "&language=en-US&page=${request.page}"

    val response = restClient.get<PopularResponseApi>(url = url)

    emit(response)
  }

  override fun fetchMultiInfo(request: MultiSearchRequestApi): Flow<MultiSearchResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/search/multi?"
    val url = baseUrl +
      "&language=en-US" +
      "&query=${request.query}" +
      "&page=${request.page}" +
      "&include_adult=false"

    val response = restClient.get<MultiSearchResponseApi>(url = url)

    emit(response)
  }

  @Deprecated("Use fetchMultiInfo instead")
  override fun fetchSearchMovies(request: SearchRequestApi): Flow<SearchResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/search/movie?"
    val url = baseUrl +
      "&language=en-US" +
      "&query=${request.query}" +
      "&page=${request.page}" +
      "&include_adult=false"

    val response = restClient.get<SearchResponseApi>(url = url)

    emit(response)
  }

  override fun fetchDetails(request: DetailsRequestApi): Flow<DetailsResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}?" +
      "&append_to_response=credits" +
      "&language=en-US"

    val response = restClient.get<DetailsResponseApi>(url = url)

    emit(response)
  }

  override fun fetchReviews(request: ReviewsRequestApi): Flow<ReviewsResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}" +
      "/reviews?" +
      "&language=en-US"

    val response = restClient.get<ReviewsResponseApi>(url = url)

    emit(response)
  }

  override fun fetchSimilarMovies(request: SimilarRequestApi): Flow<SimilarResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}" +
      "/similar?" +
      "&language=en-US"

    val response = restClient.get<SimilarResponseApi>(url = url)

    emit(response)
  }

  override fun fetchVideos(request: VideosRequestApi): Flow<VideosResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/movie/"
    val url = baseUrl +
      "${request.movieId}" +
      "/videos?" +
      "&language=en-US"

    val response = restClient.get<VideosResponseApi>(url = url)

    emit(response)
  }

  override fun fetchAccountMediaDetails(request: AccountMediaDetailsRequestApi) = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}" +
      "/account_states?" +
      "&session_id=${request.sessionId}"

    val response = restClient.get<AccountMediaDetailsResponseApi>(
      url = url
    )

    emit(response)
  }

  override fun submitRating(request: AddRatingRequestApi) = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}/rating?" +
      "&session_id=${request.sessionId}"

    val response = restClient.post<AddRatingRequestBodyApi, Unit>(
      url = url,
      body = AddRatingRequestBodyApi(request.rating)
    )

    emit(response)
  }

  override fun deleteRating(request: DeleteRatingRequestApi): Flow<Unit> = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}/rating?" +
      "&session_id=${request.sessionId}"

    val response = restClient.delete<Unit>(url = url)

    emit(response)
  }
}
