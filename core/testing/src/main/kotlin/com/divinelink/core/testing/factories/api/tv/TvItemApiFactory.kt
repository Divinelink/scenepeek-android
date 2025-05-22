package com.divinelink.core.testing.factories.api.tv

import com.divinelink.core.network.media.model.tv.TvItemApi

object TvItemApiFactory {

  fun theWire() = TvItemApi(
    id = 1438,
    adult = false,
    backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg\"",
    genreIds = listOf(80, 18),
    originCountry = listOf("US"),
    originalLanguage = "en",
    originalName = "The Wire",
    overview = "Told from the points of view of both the Baltimore homicide and " +
      "narcotics detectives and their targets, the series captures a " +
      "universe in which the national war on drugs has become a permanent," +
      " self-sustaining bureaucracy, and distinctions between good " +
      "and evil are routinely obliterated.",
    popularity = 32.9274,
    posterPath = "/4lbclFySvugI51fwsyxBTOm4DqK.jpg",
    firstAirDate = "2002-06-02",
    name = "The Wire",
    voteAverage = 8.597,
    voteCount = 2358,
  )

  fun theOffice() = TvItemApi(
    id = 2316,
    adult = false,
    backdropPath = "/mLyW3UTgi2lsMdtueYODcfAB9Ku.jpg",
    genreIds = listOf(35),
    originCountry = listOf("US"),
    originalLanguage = "en",
    originalName = "The Office",
    overview = "The everyday lives of office employees in the Scranton, " +
      "Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    popularity = 132.0914,
    posterPath = "/dg9e5fPRRId8PoBE0F6jl5y85Eu.jpg",
    firstAirDate = "2005-03-24",
    name = "The Office",
    voteAverage = 8.581,
    voteCount = 4503,
  )

  fun all() = listOf(
    theWire(),
    theOffice(),
  )
}
