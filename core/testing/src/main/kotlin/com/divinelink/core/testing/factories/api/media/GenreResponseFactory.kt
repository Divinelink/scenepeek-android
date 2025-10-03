package com.divinelink.core.testing.factories.api.media

import com.divinelink.core.network.media.model.GenreResponse

object GenreResponseFactory {

  object Movie {

    val action = GenreResponse(
      id = 28,
      name = "Action",
    )

    val adventure = GenreResponse(
      id = 12,
      name = "Adventure",
    )

    val animation = GenreResponse(
      id = 16,
      name = "Animation",
    )

    val comedy = GenreResponse(
      id = 35,
      name = "Comedy",
    )

    val crime = GenreResponse(
      id = 80,
      name = "Crime",
    )

    val documentary = GenreResponse(
      id = 99,
      name = "Documentary",
    )

    val drama = GenreResponse(
      id = 18,
      name = "Drama",
    )

    val family = GenreResponse(
      id = 10751,
      name = "Family",

    )

    val fantasy = GenreResponse(
      id = 14,
      name = "Fantasy",
    )

    val history = GenreResponse(
      id = 36,
      name = "History",
    )

    val horror = GenreResponse(
      id = 27,
      name = "Horror",
    )

    val music = GenreResponse(
      id = 10402,
      name = "Music",
    )

    val mystery = GenreResponse(
      id = 9648,
      name = "Mystery",
    )

    val romance = GenreResponse(
      id = 10749,
      name = "Romance",
    )

    val scienceFiction = GenreResponse(
      id = 878,
      name = "Science Fiction",
    )

    val tvMovie = GenreResponse(
      id = 10770,
      name = "TV Movie",
    )

    val thriller = GenreResponse(
      id = 53,
      name = "Thriller",
    )

    val war = GenreResponse(
      id = 10752,
      name = "War",
    )

    val western = GenreResponse(
      id = 37,
      name = "Western",
    )

    val all = listOf(
      action,
      adventure,
      animation,
      comedy,
      crime,
      documentary,
      drama,
      family,
      fantasy,
      history,
      horror,
      music,
      mystery,
      romance,
      scienceFiction,
      tvMovie,
      thriller,
      war,
      western,
    )
  }

  object Tv {

    val actionAdventure = GenreResponse(
      id = 10759,
      name = "Action & Adventure",
    )

    val animation = GenreResponse(
      id = 16,
      name = "Animation",
    )

    val comedy = GenreResponse(
      id = 35,
      name = "Comedy",
    )

    val crime = GenreResponse(
      id = 80,
      name = "Crime",
    )

    val documentary = GenreResponse(
      id = 99,
      name = "Documentary",
    )

    val drama = GenreResponse(
      id = 18,
      name = "Drama",
    )

    val family = GenreResponse(
      id = 10751,
      name = "Family",
    )

    val kids = GenreResponse(
      id = 10762,
      name = "Kids",
    )

    val mystery = GenreResponse(
      id = 9648,
      name = "Mystery",
    )

    val news = GenreResponse(
      id = 10763,
      name = "News",
    )

    val reality = GenreResponse(
      id = 10764,
      name = "Reality",
    )

    val sciFiFantasy = GenreResponse(
      id = 10765,
      name = "Sci-Fi & Fantasy",
    )

    val soap = GenreResponse(
      id = 10766,
      name = "Soap",
    )

    val talk = GenreResponse(
      id = 10767,
      name = "Talk",
    )

    val warPolitics = GenreResponse(
      id = 10768,
      name = "War & Politics",
    )

    val western = GenreResponse(
      id = 37,
      name = "Western",
    )

    val all = listOf(
      actionAdventure,
      animation,
      comedy,
      crime,
      documentary,
      drama,
      family,
      kids,
      mystery,
      news,
      reality,
      sciFiFantasy,
      soap,
      talk,
      warPolitics,
      western,
    )
  }
}
