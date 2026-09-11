package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.first

/**
 * Prefetches the next unwatched season when the current one is nearly complete.
 *
 * Triggers a fetch for the next missing or incomplete season if the active season
 * has 4 or fewer episodes remaining.
 */
class FetchNextMissingSeasonUseCase(
  private val repository: MediaRepository,
  private val trackRepository: TrackRepository,
  dispatcher: DispatcherProvider,
) : UseCase<Long, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Long) {
    val activeSeason = trackRepository.findCurrentWatchingSeason(parameters)
    val totalSeasons = trackRepository.fetchNumberOfSeasons(parameters)
    val gapSeason = findNextUnfinishedSeason(parameters, 0, totalSeasons)
    var syncIssue = false

    val targetSeason: Int? = when {
      // No incomplete/missing season anywhere -> probe for a new release.
      gapSeason == null -> totalSeasons + 1

      // New show, or user finished everything they had -> just fetch it.
      activeSeason == null -> gapSeason

      // Gap exists somewhere other than the season the user's on ->
      // backfill it now, regardless of episode count.
      gapSeason != activeSeason -> gapSeason

      // No gap: gapSeason == activeSeason (the season being watched).
      // Only prefetch the *next* season, and only if nearly done.
      else -> {
        val episodesLeft = trackRepository.episodesLeftThisSeason(parameters, activeSeason)
        if (episodesLeft <= 4) {
          syncIssue = episodesLeft == 1
          findNextUnfinishedSeason(parameters, activeSeason, totalSeasons)
        } else {
          null
        }
      }
    }

    if (targetSeason != null && targetSeason <= totalSeasons) {
      performFetch(parameters, targetSeason, syncIssue)
    }
  }

  private suspend fun findNextUnfinishedSeason(
    showId: Long,
    startAfter: Int,
    totalSeasons: Int,
  ): Int? {
    var candidate = startAfter + 1
    while (candidate <= totalSeasons) {
      if (!trackRepository.isSeasonComplete(showId, candidate)) {
        return candidate
      }
      candidate++
    }
    return null
  }

  private suspend fun performFetch(
    showId: Long,
    season: Int,
    syncIssue: Boolean,
  ) {
    repository.fetchSeasonDetails(showId, season, forceRefresh = false)
      .first { it is Resource.Success || it is Resource.Error }
      .let { result ->
        if (result is Resource.Error && syncIssue) {
          trackRepository.setSyncError(showId, season)
        }
      }
  }
}
