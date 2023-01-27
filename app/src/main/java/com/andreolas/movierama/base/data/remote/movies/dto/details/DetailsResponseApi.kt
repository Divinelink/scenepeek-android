package com.andreolas.movierama.base.data.remote.movies.dto.details

import com.andreolas.movierama.base.data.remote.movies.dto.details.credits.Cast
import com.andreolas.movierama.base.data.remote.movies.dto.details.credits.Crew
import com.andreolas.movierama.details.domain.model.Actor
import com.andreolas.movierama.details.domain.model.Director
import com.andreolas.movierama.details.domain.model.MovieDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class DetailsResponseApi(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("belongs_to_collection")
    val belongToCollection: JsonObject? = null,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String? = null,
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    val overview: String? = null,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerialName("release_date")
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int? = null,
    @SerialName("spoken_languages")
    val spokenLanguage: List<SpokenLanguage>,
    val status: String? = null,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val credits: Credits,
)

internal fun DetailsResponseApi.toDomainMovie(): MovieDetails {
    return MovieDetails(
        id = this.id,
        posterPath = this.posterPath ?: "",
        releaseDate = this.releaseDate,
        title = this.title,
        rating = this.voteAverage.toString(),
        overview = this.overview,
        genres = this.genres.map { it.name },
        director = this.credits.crew.toDirector(),
        cast = this.credits.cast.toActors(),
        runtime = this.runtime.toHourMinuteFormat(),
        isFavorite = false,
    )
}

private fun List<Crew>.toDirector(): Director? {
    val director = this.find { it.job == "Director" }
    return director?.let {
        Director(
            id = director.id,
            name = director.name,
            profilePath = director.profilePath ?: "",
        )
    }
}

private fun List<Cast>.toActors(): List<Actor> {
    return this.map(Cast::toActor)
}

private fun Cast.toActor(): Actor {
    return Actor(
        id = this.id,
        name = this.name,
        profilePath = this.profilePath ?: "",
        character = this.character,
        order = this.order,
    )
}

@Suppress("MagicNumber")
private fun Int?.toHourMinuteFormat(): String? {
    return this?.let { minutes ->
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return when {
            hours > 0 -> "${hours}h ${remainingMinutes}m"
            else -> "${remainingMinutes}m"
        }
    }
}

@Serializable
data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>,
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
    val originalCountry: String,
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
