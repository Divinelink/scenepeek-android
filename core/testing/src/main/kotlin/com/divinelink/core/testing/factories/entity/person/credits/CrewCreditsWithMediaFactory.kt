package com.divinelink.core.testing.factories.entity.person.credits

import com.divinelink.core.database.person.credits.CrewCreditsWithMedia
import com.divinelink.core.model.media.MediaType

object CrewCreditsWithMediaFactory {

  val the40YearOldVirgin = CrewCreditsWithMedia(
    adult = -1,
    backdropPath = "/8GWECkJcBsdReaotUFbl96gAngj.jpg",
    id = 6957,
    originalLanguage = "",
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
    voteAverage = 6.402,
    voteCount = 6545,
    creditId = "52fe446ac3a36847f8094c49",
    department = "Writing",
    job = "Screenplay",
    mediaType = MediaType.MOVIE.value,
    personId = 4495,
    firstAirDate = null,
    episodeCount = null,
    video = -1,
    name = "The 40 Year Old Virgin",
    originalName = "The 40 Year Old Virgin",
    originCountryJson = "",
    genreIdsJson = "",
  )

  val riot = CrewCreditsWithMedia(
    adult = -1,
    backdropPath = null,
    id = 60734,
    originalLanguage = "",
    originalName = "Riot",
    overview = "The show's concept places two teams of celebrities and comedians " +
      "in a series of competitions that have the teams sing, dance and create comedy sketches " +
      "while overcoming multiple mental and physical obstacles. Instructed by guest team" +
      " captains, two teams of comedians are instructed to create and participate in a " +
      "set of unscripted improv skits, some of which take place on a set tilted at 22-1/2 " +
      "degrees or some of which take place in complete darkness " +
      "with the audience able to observe through night-vision cameras" +
      " while the contestants blunder about.",
    popularity = 48.335,
    posterPath = null,
    firstAirDate = "2014-05-13",
    name = "Riot",
    voteAverage = 1.0,
    voteCount = 1,
    creditId = "53762236c3a3681ed4001579",
    department = "Production",
    job = "Executive Producer",
    episodeCount = 4,
    mediaType = MediaType.TV.value,
    genreIdsJson = "",
    personId = 4495,
    releaseDate = null,
    video = -1,
    originCountryJson = "",
  )

  val theIncredibleBurtWonderstone = CrewCreditsWithMedia(
    id = 124459,
    releaseDate = "2013-03-14",
    personId = 4495,
    firstAirDate = null,
    creditId = "5640b22e925141705c00145f",
    department = "Production",
    job = "Producer",
    episodeCount = null,
    mediaType = "movie",
    backdropPath = "/1Zoti1xJRgKMCuNKnsYLzTyNOOH.jpg",
    overview = "After breaking up with his longtime stage partner, " +
      "a famous but jaded Vegas magician fights for relevance " +
      "when a new, \"hip\" street magician appears on the scene.",
    popularity = 18.366,
    posterPath = "/Bl95sg5Ljo3Hu9SSL0JOvLerLh.jpg",
    name = "The Incredible Burt Wonderstone",
    originalName = "The Incredible Burt Wonderstone",
    voteAverage = 5.675,
    voteCount = 1389,
    adult = -1,
    originalLanguage = "",
    video = -1,
    originCountryJson = "",
    genreIdsJson = "",
  )

  val getSmart = CrewCreditsWithMedia(
    id = 11665,
    backdropPath = "/u0yaNgikY92zkI0t3fiEGZP3UWq.jpg",
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
    voteAverage = 6.191,
    voteCount = 3484,
    name = "Get Smart",
    originalName = "Get Smart",
    personId = 4495,
    firstAirDate = null,
    creditId = "52fe44749251416c750355d3",
    department = "Production",
    job = "Executive Producer",
    episodeCount = null,
    mediaType = "movie",
    adult = -1,
    originalLanguage = "",
    video = -1,
    originCountryJson = "",
    genreIdsJson = "",
  )

  val all = listOf(
    the40YearOldVirgin,
    getSmart,
    theIncredibleBurtWonderstone,
    riot,
  )

  val sortedByDate = listOf(
    riot,
    theIncredibleBurtWonderstone,
    getSmart,
    the40YearOldVirgin,
  )
}
