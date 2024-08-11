package com.andreolas.factories

import com.divinelink.core.network.media.model.details.credits.CrewApi

object CrewFactory {

  fun Director() = CrewApi(
    adult = false,
    creditId = "",
    department = "",
    gender = 0,
    id = 123443321,
    job = "Director",
    knownForDepartment = "Directing",
    name = "Forest Gump",
    originalName = "",
    popularity = 0.0,
    profilePath = "BoxOfChocolates.jpg",
  )

  fun Boomer() = CrewApi(
    adult = false,
    creditId = "",
    department = "",
    gender = 0,
    id = 123443321,
    job = "Guy with an irrelevant job",
    knownForDepartment = "Sound",
    name = "The one for the sound",
    originalName = "Boomer",
    popularity = 0.0,
    profilePath = "BoxOfChocolates.jpg",
  )

  fun all() = listOf(
    Director(),
    Boomer(),
  )
}
