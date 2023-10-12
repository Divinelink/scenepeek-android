package com.andreolas.movierama.factories

import com.andreolas.movierama.base.data.remote.movies.dto.details.credits.Cast

object CastFactory {

  fun JackNicholson() = Cast.Movie(
    id = 10,
    adult = false,
    castId = 10,
    character = "Here's Johnny!",
    creditId = "",
    gender = 0,
    knownForDepartment = "",
    name = "Jack Nicholson",
    order = 0,
    originalName = "",
    popularity = 0.0,
    profilePath = "jack_nicholson.jpg"
  )

  fun AaronPaul() = Cast.Movie(
    id = 20,
    adult = false,
    castId = 20,
    character = "Jessee Pinkman",
    creditId = "",
    gender = 0,
    knownForDepartment = "",
    name = "Aaron Paul",
    order = 1,
    originalName = "",
    popularity = 0.0,
    profilePath = "Aaron_paul.jpg"
  )

  fun all() = listOf(
    JackNicholson(),
    AaronPaul()
  )
}
