package com.divinelink.core.testing.factories.entity.person.credits

import com.divinelink.core.database.person.credits.PersonCrewCreditEntity

object PersonCrewCreditEntityFactory {

  fun the40YearOldVirgin(): PersonCrewCreditEntity = PersonCrewCreditEntity(
    adult = 0,
    backdropPath = "/8GWECkJcBsdReaotUFbl96gAngj.jpg",
    genreIds = "35, 10749",
    id = 6957,
    originalLanguage = "en",
    originalTitle = "The 40 Year Old Virgin",
    overview = "Andy Stitzer has a pleasant life with a nice apartment " +
      "and a job stamping invoices at an electronics store." +
      " But at age 40, there's one thing Andy hasn't done, " +
      "and it's really bothering his sex-obsessed male co-workers:" +
      " Andy is still a virgin. Determined to help Andy get laid," +
      " the guys make it their mission to de-virginize him. " +
      "But it all seems hopeless until Andy meets small business owner Trish, " +
      "a single mom.",
    popularity = 47.159,
    posterPath = "/mVeoqL37gzhMXQVpONi9DGOQ3tZ.jpg",
    releaseDate = "2005-08-11",
    title = "The 40 Year Old Virgin",
    video = 0,
    voteAverage = 6.402,
    voteCount = 6545,
    personId = 4495,
    originalName = null,
    firstAirDate = null,
    creditId = "52fe446ac3a36847f8094c49",
    department = "Writing",
    job = "Screenplay",
    episodeCount = null,
    mediaType = "movie",
    originCountry = null,
    name = null,
  )

  fun getSmart(): PersonCrewCreditEntity = PersonCrewCreditEntity(
    adult = 0,
    backdropPath = "/u0yaNgikY92zkI0t3fiEGZP3UWq.jpg",
    genreIds = "28, 35, 53",
    id = 11665,
    originalLanguage = "en",
    originalTitle = "Get Smart",
    overview = "When members of the nefarious crime syndicate KAOS attack the U.S. " +
      "spy agency Control and the identities of secret agents are compromised, " +
      "the Chief has to promote hapless but eager analyst" +
      " Maxwell Smart to field agent. He is partnered with veteran and capable" +
      " Agent 99, the only spy whose cover remains intact. " +
      "Can they work together to thwart the evil world-domination plans " +
      "of KAOS and its crafty operative?",
    popularity = 36.578,
    posterPath = "/sZUjbtUS8qxXp4mj90evnqPJqX7.jpg",
    releaseDate = "2008-06-19",
    title = "Get Smart",
    video = 0,
    voteAverage = 6.191,
    voteCount = 3484,
    personId = 4495,
    originalName = null,
    firstAirDate = null,
    creditId = "52fe44749251416c750355d3",
    department = "Production",
    job = "Executive Producer",
    episodeCount = null,
    mediaType = "movie",
    originCountry = null,
    name = null,
  )

  fun theIncredibleBurtWonderstone(): PersonCrewCreditEntity = PersonCrewCreditEntity(
    adult = 0,
    backdropPath = "/1Zoti1xJRgKMCuNKnsYLzTyNOOH.jpg",
    genreIds = "35",
    id = 124459,
    originalLanguage = "en",
    originalTitle = "The Incredible Burt Wonderstone",
    overview = "After breaking up with his longtime stage partner, " +
      "a famous but jaded Vegas magician fights for relevance " +
      "when a new, \"hip\" street magician appears on the scene.",
    popularity = 18.366,
    posterPath = "/Bl95sg5Ljo3Hu9SSL0JOvLerLh.jpg",
    releaseDate = "2013-03-14",
    title = "The Incredible Burt Wonderstone",
    video = 0,
    voteAverage = 5.675,
    voteCount = 1389,
    personId = 4495,
    originalName = null,
    firstAirDate = null,
    creditId = "5640b22e925141705c00145f",
    department = "Production",
    job = "Producer",
    episodeCount = null,
    mediaType = "movie",
    originCountry = null,
    name = null,
  )

  fun riot(): PersonCrewCreditEntity = PersonCrewCreditEntity(
    adult = 0,
    backdropPath = null,
    genreIds = "35",
    id = 60734,
    originCountry = "",
    originalLanguage = "en",
    originalName = "Riot",
    overview = "The show's concept places two teams of celebrities and comedians " +
      "in a series of competitions that have the teams sing, dance and create comedy sketches " +
      "while overcoming multiple mental and physical obstacles. Instructed by guest team" +
      " captains, two teams of comedians are instructed to create and participate in a " +
      "set of unscripted improv skits, some of which take place on a set tilted at 22-1/2 " +
      "degrees or some of which take place in complete darkness " +
      "with the audience able to observe through night-vision cameras" +
      " while the contestants blunder about.",
    popularity = 0.6,
    posterPath = "/1Zoti1xJRgKMCuNKnsYLzTyNOOH.jpg",
    releaseDate = "2014-03-13",
    title = "Riot",
    video = 0,
    voteAverage = 0.0,
    voteCount = 0,
    personId = 4495,
    firstAirDate = null,
    creditId = "5640b22e925141705c00145f",
    department = "Production",
    job = "Producer",
    episodeCount = null,
    mediaType = "tv",
    name = "Riot",
    originalTitle = null,
  )

  fun all() = listOf(
    the40YearOldVirgin(),
    getSmart(),
    theIncredibleBurtWonderstone(),
    riot(),
  )

  fun sortedByDate() = listOf(
    riot(),
    theIncredibleBurtWonderstone(),
    getSmart(),
    the40YearOldVirgin(),
  )
}
