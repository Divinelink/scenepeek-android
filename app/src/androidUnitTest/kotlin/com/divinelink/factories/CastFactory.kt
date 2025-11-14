package com.divinelink.factories

import com.divinelink.core.network.media.model.details.credits.CastApi

object CastFactory {

  fun JackNicholson() = CastApi.Movie(
    id = 10,
    adult = false,
    castId = 10,
    character = "Here's Johnny!",
    creditId = "",
    gender = 0,
    knownForDepartment = "Acting",
    name = "Jack Nicholson",
    order = 0,
    originalName = "",
    popularity = 0.0,
    profilePath = "jack_nicholson.jpg",
  )

  fun AaronPaul() = CastApi.Movie(
    id = 20,
    adult = false,
    castId = 20,
    character = "Jessee Pinkman",
    creditId = "",
    gender = 0,
    knownForDepartment = "Acting",
    name = "Aaron Paul",
    order = 1,
    originalName = "",
    popularity = 0.0,
    profilePath = "Aaron_paul.jpg",
  )

  fun all() = listOf(
    JackNicholson(),
    AaronPaul(),
  )
}
