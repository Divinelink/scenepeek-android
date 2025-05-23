package com.divinelink.core.network.media.model.details

import com.divinelink.core.commons.extensions.round
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.TvStatus
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.media.mapper.credits.map
import com.divinelink.core.network.media.mapper.details.map
import com.divinelink.core.network.media.model.details.credits.CastApi
import com.divinelink.core.network.media.model.details.credits.CrewApi
import com.divinelink.core.network.media.model.details.credits.SeriesCreatorApi
import com.divinelink.core.network.media.model.details.season.SeasonResponseApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable(with = DetailsResponseApiSerializer::class)
sealed class DetailsResponseApi {
  abstract val id: Int
  abstract val adult: Boolean
  abstract val genres: List<Genre>

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

  @Serializable
  data class Movie(
    override val id: Int,
    override val adult: Boolean,
    @SerialName("backdrop_path") override val backdropPath: String?,
    @SerialName("belongs_to_collection") val belongToCollection: JsonObject? = null,
    val budget: Int,
    override val genres: List<Genre>,
    val homepage: String? = null,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("original_language") override val originalLanguage: String,
    @SerialName("original_title") val originalTitle: String,
    override val overview: String?,
    override val popularity: Double,
    @SerialName("poster_path") override val posterPath: String?,
    @SerialName("production_companies") val productionCompanies: List<ProductionCompany>,
    @SerialName("production_countries") val productionCountries: List<ProductionCountry>,
    @SerialName("release_date") override val releaseDate: String,
    val revenue: Long,
    val runtime: Int? = null,
    @SerialName("spoken_languages") val spokenLanguage: List<SpokenLanguage>,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average") override val voteAverage: Double,
    @SerialName("vote_count") override val voteCount: Int,
    val credits: CreditsApi,
  ) : DetailsResponseApi()

  @Serializable
  data class TV(
    override val id: Int,
    override val adult: Boolean,
    @SerialName("backdrop_path") override val backdropPath: String?,
    override val genres: List<Genre>,
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
    @SerialName("external_ids") val externalIds: ExternalIdsApi,
  ) : DetailsResponseApi()
}

fun DetailsResponseApi.toDomainMedia(): MediaDetails = when (this) {
  is DetailsResponseApi.Movie -> this.toDomainMovie()
  is DetailsResponseApi.TV -> this.toDomainTVShow()
}

private fun DetailsResponseApi.Movie.toDomainMovie(): MediaDetails = Movie(
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
  genres = this.genres.map { it.name }.takeIf { it.isNotEmpty() },
  creators = this.credits.crew.map(),
  cast = this.credits.cast.toActors(),
  runtime = this.runtime.toHourMinuteFormat(),
  isFavorite = false,
  imdbId = this.imdbId,
)

private fun DetailsResponseApi.TV.toDomainTVShow(): MediaDetails = TV(
  id = this.id,
  posterPath = this.posterPath ?: "",
  backdropPath = this.backdropPath ?: "",
  releaseDate = this.releaseDate,
  title = this.name,
  genres = this.genres.map { it.name }.takeIf { it.isNotEmpty() },
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
  imdbId = this.externalIds.imdbId,
  status = TvStatus.from(this.status),
)

private fun List<CastApi>.toActors(): List<Person> = this.map(CastApi::toPerson)

private fun CastApi.toPerson(): Person = Person(
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
    is CastApi.TV -> listOf(PersonRole.Unknown)
  },
)

@Suppress("MagicNumber")
private fun Int?.toHourMinuteFormat(): String? {
  return this?.let { minutes ->
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return when {
      hours > 0 -> "${hours}h ${remainingMinutes}m"
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
data class Genre(
  val id: Int,
  val name: String,
)

@Serializable
data class ProductionCompany(
  val id: Int,
  @SerialName("logo_path")
  val logoPath: String? = null,
  val name: String,
  @SerialName("origin_country")
  val originalCountry: String? = null,
)

@Serializable
data class ProductionCountry(
  @SerialName("iso_3166_1")
  val iso31611: String,
  val name: String,
)

@Serializable
data class SpokenLanguage(
  @SerialName("iso_639_1")
  val iso6391: String,
  @SerialName("english_name")
  val englishName: String,
  val name: String,
)
