package com.divinelink.core.data.details.repository

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.data.details.mapper.api.map
import com.divinelink.core.data.details.mapper.api.reviews.map
import com.divinelink.core.data.details.mapper.api.toSeriesCastEntity
import com.divinelink.core.data.details.mapper.api.toSeriesCastRoleEntity
import com.divinelink.core.data.details.mapper.api.toSeriesCrewEntity
import com.divinelink.core.data.details.mapper.api.toSeriesCrewJobEntity
import com.divinelink.core.data.details.mapper.map
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.ReviewsException
import com.divinelink.core.data.details.model.VideosException
import com.divinelink.core.database.credits.dao.CreditsDao
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.model.details.toMediaItem
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.mapper.find.map
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.network.media.model.details.toDomainMedia
import com.divinelink.core.network.media.model.details.videos.toDomainVideosList
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.movie.map
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.media.model.tv.map
import com.divinelink.core.network.media.service.MediaService
import com.divinelink.core.network.omdb.mapper.map
import com.divinelink.core.network.omdb.service.OMDbService
import com.divinelink.core.network.trakt.mapper.map
import com.divinelink.core.network.trakt.service.TraktService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import kotlin.time.measureTime

class ProdDetailsRepository(
  private val mediaRemote: MediaService,
  private val creditsDao: CreditsDao,
  private val omdbService: OMDbService,
  private val traktService: TraktService,
  private val mediaDao: MediaDao,
  val dispatcher: DispatcherProvider,
) : DetailsRepository {

  override fun fetchMediaDetails(request: MediaRequestApi): Flow<Result<MediaDetails>> = mediaRemote
    .fetchDetails(request)
    .map { apiResponse ->
      val details = apiResponse.toDomainMedia()
      val mediaItem = details.toMediaItem()

      mediaDao.insertMedia(mediaItem)
      Result.success(details)
    }.catch {
      throw MediaDetailsException()
    }

  override fun fetchMediaReviews(request: MediaRequestApi): Flow<Result<List<Review>>> = mediaRemote
    .fetchReviews(request)
    .map { apiResponse ->
      Result.success(apiResponse.map())
    }.catch {
      throw ReviewsException()
    }

  override fun fetchRecommendedMovies(
    request: MediaRequestApi.Movie,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    mediaRemote.fetchRecommendedMovies(request),
    mediaDao.getFavoriteMediaIds(MediaType.MOVIE),
  ) { response, favoriteIds ->
    val mapped = response.map()
    val favoriteSet = favoriteIds.toSet()

    val updatedMovies = mapped.list.map { media ->
      (media as MediaItem.Media.Movie).copy(
        isFavorite = media.id in favoriteSet,
      )
    }

    Result.success(mapped.copy(list = updatedMovies))
  }.catch { Timber.d(it) }

  override fun fetchRecommendedTv(
    request: MediaRequestApi.TV,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    mediaRemote.fetchRecommendedTv(request),
    mediaDao.getFavoriteMediaIds(MediaType.TV),
  ) { response, favoriteIds ->
    val mapped = response.map()
    val favoriteSet = favoriteIds.toSet()

    val updatedTv = mapped.list.map { media ->
      (media as MediaItem.Media.TV).copy(
        isFavorite = media.id in favoriteSet,
      )
    }

    Result.success(mapped.copy(list = updatedTv))
  }.catch { Timber.d(it) }

  override fun fetchVideos(request: MediaRequestApi): Flow<Result<List<Video>>> = mediaRemote
    .fetchVideos(request)
    .map { apiResponse ->
      Result.success(apiResponse.toDomainVideosList())
    }.catch {
      throw VideosException()
    }

  override fun fetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi,
  ): Flow<Result<AccountMediaDetails>> = mediaRemote
    .fetchAccountMediaDetails(request)
    .map { response ->
      Result.success(response.map())
    }

  override suspend fun submitRating(request: AddRatingRequestApi): Result<Unit> = mediaRemote
    .submitRating(request)
    .map { it.success }

  override suspend fun deleteRating(request: DeleteRatingRequestApi): Result<Unit> = mediaRemote
    .deleteRating(request)
    .map { it.success }

  override suspend fun addToWatchlist(request: AddToWatchlistRequestApi): Result<Unit> = mediaRemote
    .addToWatchlist(request)
    .map { it.success }

  override fun fetchAggregateCredits(id: Long): Flow<Result<AggregateCredits>> = flow {
    val localExists = creditsDao.checkIfAggregateCreditsExist(id).first()
    if (localExists) {
      Timber.d("Fetching local credits")
      fetchLocalAggregateCredits(id).collect {
        emit(it)
      }
    } else {
      Timber.d("Fetching remote credits")
      fetchRemoteAggregateCredits(id).collect {
        emit(it)
      }
    }
  }.flowOn(dispatcher.io)

  private fun insertLocalAggregateCredits(aggregateCredits: AggregateCreditsApi) {
    creditsDao.insertAggregateCredits(aggregateCredits.id)
    creditsDao.insertCastRoles(aggregateCredits.toSeriesCastRoleEntity())
    creditsDao.insertCast(aggregateCredits.toSeriesCastEntity())
    creditsDao.insertCrewJobs(aggregateCredits.toSeriesCrewJobEntity())
    creditsDao.insertCrew(aggregateCredits.toSeriesCrewEntity())
  }

  private fun fetchLocalAggregateCredits(id: Long): Flow<Result<AggregateCredits>> = creditsDao
    .fetchAllCredits(id)
    .map { localCredits ->
      Result.success(localCredits.map())
    }

  private fun fetchRemoteAggregateCredits(id: Long): Flow<Result<AggregateCredits>> =
    mediaRemote.fetchAggregatedCredits(id)
      .onEach { apiResponse ->
        val duration = measureTime {
          insertLocalAggregateCredits(apiResponse)
        }
        Timber.d("Inserting credits took $duration")
      }
      .map { apiResponse ->
        Result.success(apiResponse.map())
      }

  override fun fetchIMDbDetails(imdbId: String): Flow<Result<RatingDetails?>> = omdbService
    .fetchImdbDetails(imdbId = imdbId)
    .map { Result.success(it.map()) }

  override fun fetchTraktRating(
    mediaType: MediaType,
    imdbId: String,
  ): Flow<Result<RatingDetails>> = traktService
    .fetchRating(mediaType = mediaType, imdbId = imdbId)
    .map { Result.success(it.map()) }

  override fun findById(id: String): Flow<Result<MediaItem>> = mediaRemote
    .findById(id)
    .map {
      Result.success(it.map())
    }
}
