package com.divinelink.core.network.media.model.details

import com.divinelink.core.commons.extensions.formatWithCommas
import com.divinelink.core.commons.extensions.round
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.media.MediaDetailsInformation
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.media.mapper.credits.map
import com.divinelink.core.network.media.mapper.details.map
import com.divinelink.core.network.media.mapper.map
import com.divinelink.core.network.media.model.GenreResponse
import com.divinelink.core.network.media.model.details.credits.CastApi
import com.divinelink.core.network.media.model.details.credits.CrewApi
import com.divinelink.core.network.media.model.details.credits.SeriesCreatorApi
import com.divinelink.core.network.media.model.details.season.SeasonResponseApi
import com.divinelink.core.network.media.model.details.tv.NextEpisodeToAirResponse
import com.divinelink.core.network.media.model.movie.BelongsToCollectionResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = DetailsResponseApiSerializer::class)
sealed class DetailsResponseApi {
  abstract val id: Long
  abstract val adult: Boolean
  abstract val genres: List<GenreResponse>

  @SerialName("backdrop_path")
  abstract val backdropPath: String?
  abstract val originalLanguage: String
  abstract val overview: String?
  abstract val popularity: Double

  @SerialName("poster_path")
  abstract val posterPath: String?

  @SerialName("vote_average")
  abstract val voteAverage: Double

  @SerialName("vote_count")
  abstract val voteCount: Int
  abstract val releaseDate: String

  @SerialName("production_companies")
  abstract val companies: List<ProductionCompany>

  @SerialName("production_countries")
  abstract val countries: List<ProductionCountryResponse>

  abstract val keywords: KeywordsResponse?

  @Serializable
  data class Movie(
    override val id: Long,
    override val adult: Boolean,
    @SerialName("backdrop_path") override val backdropPath: String?,
    val budget: Int,
    override val genres: List<GenreResponse>,
    val homepage: String? = null,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("original_language") override val originalLanguage: String,
    @SerialName("original_title") val originalTitle: String,
    override val overview: String?,
    override val popularity: Double,
    @SerialName("poster_path") override val posterPath: String?,
    @SerialName("production_companies") override val companies: List<ProductionCompany>,
    @SerialName("production_countries") override val countries: List<ProductionCountryResponse>,
    @SerialName("release_date") override val releaseDate: String,
    val revenue: Long,
    val runtime: Int? = null,
    @SerialName("spoken_languages") val spokenLanguage: List<SpokenLanguageResponse>,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average") override val voteAverage: Double,
    @SerialName("vote_count") override val voteCount: Int,
    val credits: CreditsApi? = null, // TODO credits call should be made separately
    val status: String? = null,
    @SerialName("belongs_to_collection") val collection: BelongsToCollectionResponse?,
    override val keywords: KeywordsResponse?,
    @SerialName("release_dates") val releaseDates: ReleaseDatesResponse?,
  ) : DetailsResponseApi()

  @Serializable
  data class TV(
    override val id: Long,
    override val adult: Boolean,
    @SerialName("backdrop_path") override val backdropPath: String?,
    override val genres: List<GenreResponse>,
    @SerialName("origin_country") val originCountry: List<String>,
    @SerialName("original_language") override val originalLanguage: String,
    @SerialName("original_name") val originalName: String,
    val tagline: String,
    override val overview: String,
    override val popularity: Double,
    @SerialName("poster_path") override val posterPath: String?,
    @SerialName("first_air_date") override val releaseDate: String,
    val name: String,
    val status: String? = null,
    @SerialName("vote_average") override val voteAverage: Double,
    @SerialName("vote_count") override val voteCount: Int,
    @SerialName("created_by") val createdBy: List<SeriesCreatorApi>,
    @SerialName("seasons") val seasons: List<SeasonResponseApi>,
    @SerialName("number_of_seasons") val numberOfSeasons: Int,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int,
    @SerialName("external_ids") val externalIds: ExternalIdsApi? = null,
    @SerialName("last_air_date") val lastAirDate: String? = null,
    @SerialName("next_episode_to_air") val nextEpisodeToAir: NextEpisodeToAirResponse? = null,
    @SerialName("production_companies") override val companies: List<ProductionCompany>,
    @SerialName("production_countries") override val countries: List<ProductionCountryResponse>,
    override val keywords: KeywordsResponse?,
  ) : DetailsResponseApi()
}

fun DetailsResponseApi.toDomainMedia(selectedRegion: Country?): MediaDetails = when (this) {
  is DetailsResponseApi.Movie -> this.toDomainMovie(selectedRegion)
  is DetailsResponseApi.TV -> this.toDomainTVShow()
}

private fun DetailsResponseApi.Movie.toDomainMovie(selectedRegion: Country?): MediaDetails = Movie(
  id = this.id,
  posterPath = this.posterPath ?: "",
  backdropPath = this.backdropPath ?: "",
  releaseDate = this.releaseDate,
  title = this.title,
  ratingCount = RatingCount.tmdb(
    tmdbVoteAverage = this.voteAverage.round(1),
    tmdbVoteCount = voteCount,
  ),
  tagline = this.tagline.takeIf { it.isNotBlank() },
  overview = this.overview,
  genres = this.genres.map { it.map() },
  creators = this.credits?.crew?.map(),
  cast = this.credits?.cast?.toActors() ?: emptyList(),
  runtime = this.runtime.toHourMinuteFormat(),
  isFavorite = false,
  imdbId = this.imdbId,
  popularity = popularity,
  collection = collection?.map(),
  information = MediaDetailsInformation.Movie(
    originalTitle = originalTitle,
    status = status ?: "-",
    runtime = runtime.toHourMinuteFormat(),
    originalLanguage = Language.fromCode(originalLanguage),
    countries = this.countries.mapNotNull {
      Country.fromCode(it.iso31611)
    },
    companies = this.companies.map { it.name },
    budget = if (budget == 0) {
      "-"
    } else {
      "$${budget.formatWithCommas()}"
    },
    revenue = if (revenue == 0L) {
      "-"
    } else {
      "$${revenue.formatWithCommas()}"
    },
    releaseDates = releaseDates.map(selectedRegion),
  ),
  keywords = keywords.map(),
)

private fun DetailsResponseApi.TV.toDomainTVShow(): MediaDetails = TV(
  id = this.id,
  posterPath = this.posterPath ?: "",
  backdropPath = this.backdropPath ?: "",
  releaseDate = this.releaseDate,
  title = this.name,
  genres = this.genres.map { it.map() },
  ratingCount = RatingCount.tmdb(
    tmdbVoteAverage = this.voteAverage.round(1),
    tmdbVoteCount = voteCount,
  ),
  tagline = this.tagline.takeIf { it.isNotBlank() },
  overview = this.overview,
  isFavorite = false,
  numberOfSeasons = this.numberOfSeasons,
  creators = this.createdBy.map(),
  seasons = this.seasons.map(),
  imdbId = this.externalIds?.imdbId,
  popularity = popularity,
  information = MediaDetailsInformation.TV(
    originalTitle = this.originalName,
    status = TvStatus.from(this.status),
    firstAirDate = this.releaseDate,
    lastAirDate = lastAirDate ?: "-",
    nextEpisodeAirDate = nextEpisodeToAir?.airDate,
    seasons = numberOfSeasons,
    episodes = numberOfEpisodes,
    originalLanguage = Language.fromCode(originalLanguage),
    companies = this.companies.map { it.name },
    countries = this.countries.mapNotNull {
      Country.fromCode(it.iso31611)
    },
  ),
  keywords = keywords.map(),
)

private fun List<CastApi>.toActors(): List<MediaItem.Person> = this.map(CastApi::toPerson)

fun CastApi.toPerson() = MediaItem.Person(
  id = this.id,
  name = this.name,
  profilePath = this.profilePath,
  knownForDepartment = this.knownForDepartment,
  gender = Gender.from(this.gender),
  role = when (this) {
    is CastApi.Movie -> {
      listOf(
        PersonRole.MovieActor(
          character = this.character,
          order = this.order,
        ),
      )
    }
    is CastApi.TV -> listOf(
      PersonRole.SeriesActor(
        character = character,
        creditId = creditId,
        order = order,
      ),
    )
  },
)

@Suppress("MagicNumber")
fun Int?.toHourMinuteFormat(): String? {
  return this?.let { minutes ->
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return when {
      hours > 0 && remainingMinutes > 0 -> "${hours}h ${remainingMinutes}m"
      hours > 0 -> "${hours}h"
      remainingMinutes > 0 -> "${remainingMinutes}m"
      else -> null
    }
  }
}

@Serializable
data class CreditsApi(
  val cast: List<CastApi>,
  val crew: List<CrewApi>,
)

@Serializable
data class ProductionCompany(
  val id: Int,
  @SerialName("logo_path") val logoPath: String? = null,
  val name: String,
  @SerialName("origin_country") val originalCountry: String? = null,
)

@Serializable
data class ProductionCountryResponse(
  @SerialName("iso_3166_1") val iso31611: String,
  val name: String,
)

@Serializable
data class SpokenLanguageResponse(
  @SerialName("iso_639_1") val iso6391: String,
  @SerialName("english_name") val englishName: String,
  val name: String,
)
