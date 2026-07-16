@file:Suppress("TooManyFunctions")

package com.divinelink.core.database.media.dao

import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.database.season.SeasonDetailsEntity
import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaDao {

  fun fetchMedia(media: MediaReference): MediaItem.Media?

  fun insertMedia(
    media: MediaItem.Media,
    seasons: List<Season>? = null,
  )

  fun insertMediaEntities(media: List<MediaItemEntity>)

  fun insertMediaList(media: List<MediaItem.Media>)

  fun fetchAllFavorites(): Flow<List<MediaItem>>

  fun fetchFavorites(mediaType: MediaType): Flow<List<MediaItem>>

  fun getFavoriteMediaIds(mediaType: MediaType): Flow<List<Long>>

  fun fetchFavoritePeople(): Flow<List<MediaItem.Person>>

  fun insertSeasons(
    id: Long,
    seasons: List<Season>,
  )

  fun updateSeasonStatus(
    mediaId: Long,
    seasons: List<SeasonRequest>,
    override: Boolean,
  )

  fun fetchSeasons(id: Long): Flow<List<Season>>

  fun fetchSeason(
    showId: Long,
    seasonNumber: Int,
  ): Flow<Season>

  fun addToFavorites(
    mediaId: Long,
    mediaType: MediaType,
  )

  fun removeFromFavorites(
    mediaId: Long,
    mediaType: MediaType,
  )

  fun isMediaFavorite(
    mediaId: Long,
    mediaType: MediaType,
  ): Boolean

  fun fetchGenres(mediaType: MediaType): Flow<List<Genre>>

  fun insertGenres(
    mediaType: MediaType,
    genres: List<Genre>,
  )

  fun insertEpisodes(episodes: List<Episode>)

  fun fetchEpisode(
    showId: Long,
    episodeNumber: Int,
    seasonNumber: Int,
  ): Flow<Episode>

  fun fetchEpisodes(
    showId: Long,
    season: Int,
  ): Flow<List<Episode>>

  fun insertSeasonDetails(
    seasonDetails: SeasonDetails,
    showId: Long,
    seasonNumber: Int,
  )

  fun fetchSeasonDetails(
    showId: Long,
    season: Int,
  ): Flow<SeasonDetailsEntity?>

  fun fetchSeasonEpisodesCount(
    season: Int,
    showId: Long,
  ): List<Int>

  fun insertEpisodeRating(
    showId: Long,
    season: Int,
    number: Int,
    rating: Int,
  )

  fun deleteEpisodeRating(
    showId: Long,
    season: Int,
    number: Int,
  )

  fun clearAllEpisodeRatings()
}
