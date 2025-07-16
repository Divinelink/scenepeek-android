package com.divinelink.core.fixtures.model.list

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.list.ListDetails

object ListDetailsFactory {

  fun empty() = ListDetails(
    page = 1,
    backdropPath = "",
    name = "Must watch",
    media = emptyList(),
    totalPages = 1,
    totalResults = 0,
  )

  fun mustWatch() = ListDetails(
    page = 1,
    backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
    name = "Must watch",
    media = listOf(
      MediaItemFactory.theWire(),
      MediaItemFactory.FightClub(),
      MediaItemFactory.theOffice(),
    ),
    totalPages = 1,
    totalResults = 3,
  )

  fun page1() = ListDetails(
    page = 1,
    backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
    name = "Top rated movies",
    media = MediaItemFactory.MoviesList(range = 1..20),
    totalPages = 2,
    totalResults = 40,
  )

  fun page2() = ListDetails(
    page = 2,
    backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
    name = "Top rated movies",
    media = MediaItemFactory.MoviesList(range = 21..40),
    totalPages = 2,
    totalResults = 40,
  )
}
