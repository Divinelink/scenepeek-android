package com.divinelink.core.fixtures.model.list

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.list.ListDetails

object ListDetailsFactory {

  fun empty() = ListDetails(
    page = 1,
    backdropPath = "",
    description = "",
    name = "Must watch",
    media = emptyList(),
    totalPages = 1,
    totalResults = 0,
    public = false,
    id = -1,
    averageRating = 0.0,
    posterPath = null,
    itemCount = 0,
    revenue = 0,
    runtime = 0,
    sortBy = "",
    iso31661 = "",
    iso6391 = "",
    createdBy = CreatedByUserFactory.empty(),
  )

  fun mustWatch() = ListDetails(
    id = 8542884,
    page = 1,
    backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
    description = "A list of must watch movies and shows",
    name = "Must watch",
    media = listOf(
      MediaItemFactory.theWire(),
      MediaItemFactory.FightClub(),
      MediaItemFactory.theOffice(),
    ),
    totalPages = 1,
    public = false,
    totalResults = 3,
    averageRating = 8.5,
    posterPath = null,
    itemCount = 3,
    revenue = 100853753,
    runtime = 139,
    sortBy = "original_order.asc",
    iso31661 = "US",
    iso6391 = "en",
    createdBy = CreatedByUserFactory.elsa(),
  )

  fun page1() = ListDetails(
    id = 1,
    page = 1,
    backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
    description = "A list of top rated movies",
    name = "Top rated movies",
    media = MediaItemFactory.MoviesList(range = 1..20),
    totalPages = 2,
    totalResults = 40,
    public = true,
    averageRating = 8.0,
    posterPath = null,
    itemCount = 40,
    revenue = 1234567890,
    runtime = 543,
    sortBy = "original_order.asc",
    iso31661 = "US",
    iso6391 = "en",
    createdBy = CreatedByUserFactory.elsa(),
  )

  fun page2() = ListDetails(
    id = 1,
    page = 2,
    backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
    description = "A list of top rated movies",
    name = "Top rated movies",
    media = MediaItemFactory.MoviesList(range = 21..40),
    totalPages = 2,
    totalResults = 40,
    public = true,
    averageRating = 8.0,
    posterPath = null,
    itemCount = 40,
    revenue = 1234567890,
    runtime = 543,
    sortBy = "original_order.asc",
    iso31661 = "US",
    iso6391 = "en",
    createdBy = CreatedByUserFactory.elsa(),
  )
}
