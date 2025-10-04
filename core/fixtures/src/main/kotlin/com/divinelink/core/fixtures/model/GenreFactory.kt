package com.divinelink.core.fixtures.model

import com.divinelink.core.model.Genre

object GenreFactory {

  object Movie {

    val action = Genre(
      id = 28,
      name = "Action",
    )

    val adventure = Genre(
      id = 12,
      name = "Adventure",
    )

    val animation = Genre(
      id = 16,
      name = "Animation",
    )

    val comedy = Genre(
      id = 35,
      name = "Comedy",
    )

    val crime = Genre(
      id = 80,
      name = "Crime",
    )

    val documentary = Genre(
      id = 99,
      name = "Documentary",
    )

    val drama = Genre(
      id = 18,
      name = "Drama",
    )

    val family = Genre(
      id = 10751,
      name = "Family",

    )

    val fantasy = Genre(
      id = 14,
      name = "Fantasy",
    )

    val history = Genre(
      id = 36,
      name = "History",
    )

    val horror = Genre(
      id = 27,
      name = "Horror",
    )

    val music = Genre(
      id = 10402,
      name = "Music",
    )

    val mystery = Genre(
      id = 9648,
      name = "Mystery",
    )

    val romance = Genre(
      id = 10749,
      name = "Romance",
    )

    val scienceFiction = Genre(
      id = 878,
      name = "Science Fiction",
    )

    val tvMovie = Genre(
      id = 10770,
      name = "TV Movie",
    )

    val thriller = Genre(
      id = 53,
      name = "Thriller",
    )

    val war = Genre(
      id = 10752,
      name = "War",
    )

    val western = Genre(
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

    val actionAdventure = Genre(
      id = 10759,
      name = "Action & Adventure",
    )

    val animation = Genre(
      id = 16,
      name = "Animation",
    )

    val comedy = Genre(
      id = 35,
      name = "Comedy",
    )

    val crime = Genre(
      id = 80,
      name = "Crime",
    )

    val documentary = Genre(
      id = 99,
      name = "Documentary",
    )

    val drama = Genre(
      id = 18,
      name = "Drama",
    )

    val family = Genre(
      id = 10751,
      name = "Family",
    )

    val kids = Genre(
      id = 10762,
      name = "Kids",
    )

    val mystery = Genre(
      id = 9648,
      name = "Mystery",
    )

    val news = Genre(
      id = 10763,
      name = "News",
    )

    val reality = Genre(
      id = 10764,
      name = "Reality",
    )

    val sciFiFantasy = Genre(
      id = 10765,
      name = "Sci-Fi & Fantasy",
    )

    val soap = Genre(
      id = 10766,
      name = "Soap",
    )

    val talk = Genre(
      id = 10767,
      name = "Talk",
    )

    val warPolitics = Genre(
      id = 10768,
      name = "War & Politics",
    )

    val western = Genre(
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
