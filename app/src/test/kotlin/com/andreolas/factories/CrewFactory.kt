package com.andreolas.factories

import com.divinelink.core.network.media.model.details.credits.Crew

object CrewFactory {

  fun Director() = Crew(
    adult = false,
    creditId = "",
    department = "",
    gender = 0,
    id = 123443321,
    job = "Director",
    knownForDepartment = "",
    name = "Forest Gump",
    originalName = "",
    popularity = 0.0,
    profilePath = "BoxOfChocolates.jpg"
  )

  fun Boomer() = Crew(
    adult = false,
    creditId = "",
    department = "",
    gender = 0,
    id = 123443321,
    job = "Guy with an irrelevant job",
    knownForDepartment = "",
    name = "The one for the sound",
    originalName = "Boomer",
    popularity = 0.0,
    profilePath = "BoxOfChocolates.jpg"
  )

  fun all() = listOf(
    Director(),
    Boomer(),
  )
}
