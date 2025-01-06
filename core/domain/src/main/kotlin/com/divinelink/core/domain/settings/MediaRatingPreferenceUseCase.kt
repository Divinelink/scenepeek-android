package com.divinelink.core.domain.settings

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.details.rating.MediaRatingSource
import com.divinelink.core.model.details.rating.RatingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MediaRatingPreferenceUseCase(
  private val storage: PreferenceStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Pair<MediaRatingSource, RatingSource>>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<Pair<MediaRatingSource, RatingSource>>> =
    channelFlow {
      launch(dispatcher.io) {
        storage.movieRatingSource.collect { movieSource ->
          Timber.d("Movie rating source: $movieSource")
          send(Result.success(MediaRatingSource.Movie to movieSource))
        }
      }

      launch(dispatcher.io) {
        storage.tvRatingSource.collect { tvSource ->
          Timber.d("TV rating source: $tvSource")
          send(Result.success(MediaRatingSource.TVShow to tvSource))
        }
      }
    }

  suspend fun setMediaRatingSource(source: Pair<MediaRatingSource, RatingSource>) {
    when (source.first) {
      MediaRatingSource.Episodes -> TODO()
      MediaRatingSource.Movie -> storage.setMovieRatingSource(source.second)
      MediaRatingSource.Seasons -> TODO()
      MediaRatingSource.TVShow -> storage.setTvRatingSource(source.second)
    }
  }
}
