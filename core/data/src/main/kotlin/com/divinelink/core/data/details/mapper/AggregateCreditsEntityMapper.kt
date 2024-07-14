package com.divinelink.core.data.details.mapper

import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.database.credits.model.CastEntity
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.network.media.model.credits.SeriesCastApi
import com.divinelink.core.network.media.model.credits.SeriesCrewApi

//fun AggregateCreditsApi.toDatabaseModel(): AggregateCreditsEntity = AggregateCreditsEntity(
//  id = this.id,
//  cast = this.cast.map { it.toDatabaseModel() },
//  crew = this.crew.map { it.toDatabaseModel() },
//)
//
//fun SeriesCastApi.toDatabaseModel(aggregateCreditId: Int): CastEntity = CastEntity(
//  id = this.id,
//  adult = if (this.adult) 1 else 0,
//  gender = this.gender.toLong(),
//  name = this.name,
//  originalName = this.originalName,
//  knownForDepartment = this.knownForDepartment,
//  popularity = this.popularity,
//  profilePath = this.profilePath,
//  totalEpisodeCount = this.totalEpisodeCount.toLong(),
//  orderIndex = this.order.toLong(),
//)
//
//fun SeriesCrewApi.toDatabaseModel(aggregateCreditId: Int): CrewEntity = CrewEntity(
//  id = this.id,
//  adult = if (this.adult) 1 else 0,
//  gender = this.gender,
//  name = this.name,
//  originalName = this.original_name,
//  knownForDepartment = this.known_for_department,
//  popularity = this.popularity,
//  profilePath = this.profile_path,
//  department = this.department,
//  totalEpisodeCount = this.total_episode_count,
//  aggregateCreditId = aggregateCreditId,
//)
//
//fun RoleApi.toDatabaseModel(
//  castId: Int,
//  aggregateCreditId: Int,
//): RoleEntity = RoleEntity(
//  creditId = this.credit_id,
//  character = this.character,
//  episodeCount = this.episode_count,
//  castId = castId,
//  aggregateCreditId = aggregateCreditId,
//)
//
//fun JobApi.toDatabaseModel(
//  crewId: Int,
//  aggregateCreditId: Int,
//): JobEntity = JobEntity(
//  creditId = this.credit_id,
//  job = this.job,
//  episodeCount = this.episode_count,
//  crewId = crewId,
//  aggregateCreditId = aggregateCreditId,
//)
