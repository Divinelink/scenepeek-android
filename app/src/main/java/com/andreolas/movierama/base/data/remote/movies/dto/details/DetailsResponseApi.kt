package com.andreolas.movierama.base.data.remote.movies.dto.details

import com.andreolas.movierama.base.data.remote.movies.dto.details.credits.Cast
import com.andreolas.movierama.base.data.remote.movies.dto.details.credits.Crew
import com.andreolas.movierama.details.domain.model.Actor
import com.andreolas.movierama.details.domain.model.Director
import com.andreolas.movierama.details.domain.model.MovieDetails
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailsResponseApi(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String,
    @SerialName("belongs_to_collection")
    @Contextual val belongToCollection: Any? = null,
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
    val revenue: Int,
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
    val cast: List<Cast>,
    val crew: List<Crew>,
)

internal fun DetailsResponseApi.toDomainMovie(): MovieDetails {
    return MovieDetails(
        id = this.id,
        posterPath = this.posterPath ?: "",
        releaseDate = this.releaseDate,
        title = this.title,
        rating = this.voteAverage.toString(),
        overview = this.overview,
        director = this.crew.toDirector(),
        cast = this.cast.toActors(),
        isFavorite = false,
    )
}

internal fun List<Crew>.toDirector(): Director? {
    val director = this.find { it.job == "Director" }
    return director?.let {
        Director(
            id = director.id,
            name = director.name,
            profilePath = director.profilePath,
        )
    }
}

internal fun List<Cast>.toActors(): List<Actor> {
    return this.map(Cast::toActor)
}

internal fun Cast.toActor(): Actor {
    return Actor(
        id = this.id,
        name = this.name,
        profilePath = this.profilePath,
        character = this.character,
        order = this.order,
    )
}

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
    val name: String,
)
