package com.andreolas.movierama.base.data.remote.movies.service

import com.andreolas.movierama.base.communication.ApiConstants
import com.andreolas.movierama.base.communication.RestClient
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.multi.MultiSearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.multi.MultiSearchResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdMovieService @Inject constructor(
  private val restClient: RestClient,
) : MovieService {

  override fun fetchPopularMovies(request: PopularRequestApi): Flow<PopularResponseApi> = flow {
    val baseUrl = "${ApiConstants.TMDB_URL}/movie/popular?"
    val url = baseUrl + "&language=en-US&page=${request.page}"

    val response = restClient.get<PopularResponseApi>(url = url)

    emit(response)
  }

  override fun fetchMultiInfo(request: MultiSearchRequestApi): Flow<MultiSearchResponseApi> = flow {
    val baseUrl = "${ApiConstants.TMDB_URL}/search/multi?"
    val url = baseUrl +
      "&language=en-US" +
      "&query=${request.query}" +
      "&page=${request.page}" +
      "&include_adult=false"

    val response = restClient.get<MultiSearchResponseApi>(url = url)

    emit(response)
  }

  override fun fetchSearchMovies(request: SearchRequestApi): Flow<SearchResponseApi> = flow {
    val baseUrl = "${ApiConstants.TMDB_URL}/search/movie?"
    val url = baseUrl +
      "&language=en-US" +
      "&query=${request.query}" +
      "&page=${request.page}" +
      "&include_adult=false"

    val response = restClient.get<SearchResponseApi>(url = url)

    emit(response)
  }

  override fun fetchDetails(request: DetailsRequestApi): Flow<DetailsResponseApi> = flow {
    val baseUrl = "${ApiConstants.TMDB_URL}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}?" +
      "&append_to_response=credits" +
      "&language=en-US"

    val response = restClient.get<DetailsResponseApi>(url = url)

    emit(response)
  }

  override fun fetchReviews(request: ReviewsRequestApi): Flow<ReviewsResponseApi> = flow {
    val baseUrl = "${ApiConstants.TMDB_URL}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}" +
      "/reviews?" +
      "&language=en-US"

    val response = restClient.get<ReviewsResponseApi>(url = url)

    emit(response)
  }

  override fun fetchSimilarMovies(request: SimilarRequestApi): Flow<SimilarResponseApi> = flow {
    val baseUrl = "${ApiConstants.TMDB_URL}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}" +
      "/similar?" +
      "&language=en-US"

    val response = restClient.get<SimilarResponseApi>(url = url)

    emit(response)
  }

  override fun fetchVideos(request: VideosRequestApi): Flow<VideosResponseApi> = flow {
    val baseUrl = "${ApiConstants.TMDB_URL}/movie/"
    val url = baseUrl +
      "${request.movieId}" +
      "/videos?" +
      "&language=en-US"

    val response = restClient.get<VideosResponseApi>(url = url)

    emit(response)
  }
}
