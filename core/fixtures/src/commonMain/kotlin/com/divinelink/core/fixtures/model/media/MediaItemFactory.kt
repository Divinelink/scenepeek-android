package com.divinelink.core.fixtures.model.media

import com.divinelink.core.fixtures.LoremIpsum
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender

object MediaItemFactory {

  fun FightClub(): MediaItem.Media.Movie = MediaItem.Media.Movie(
    id = 550,
    posterPath = "/jSziioSwPVrOy9Yow3XhWIBDjq1.jpg",
    backdropPath = "/xRyINp9KfMLVjRiO5nCsoRDdvvF.jpg",
    releaseDate = "1999-10-15",
    name = "Fight Club",
    voteAverage = 8.4,
    voteCount = 30_452,
    popularity = 21.6213,
    overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel " +
      "primal male aggression into a shocking new form of therapy." +
      " Their concept catches on, with underground \"fight clubs\" forming in every town, " +
      "until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
    isFavorite = false,
  )

  fun theOffice(): MediaItem.Media.TV = MediaItem.Media.TV(
    id = 2316,
    overview = "The everyday lives of office employees in the Scranton, " +
      "Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    posterPath = "/7DJKHzAi83BmQrWLrYYOqcoKfhR.jpg",
    backdropPath = "/mLyW3UTgi2lsMdtueYODcfAB9Ku.jpg",
    releaseDate = "2005-03-24",
    name = "The Office",
    popularity = 530.095,
    voteAverage = 8.6,
    voteCount = 4503,
    isFavorite = false,
  )

  fun theWire() = MediaItem.Media.TV(
    id = 1438,
    overview = "Told from the points of view of both the Baltimore homicide and " +
      "narcotics detectives and their targets, the series captures a " +
      "universe in which the national war on drugs has become a permanent," +
      " self-sustaining bureaucracy, and distinctions between good " +
      "and evil are routinely obliterated.",
    backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
    posterPath = "/4lbclFySvugI51fwsyxBTOm4DqK.jpg",
    releaseDate = "2002-06-02",
    name = "The Wire",
    voteAverage = 8.6,
    voteCount = 2358,
    popularity = 72.3288,
    isFavorite = false,
  )

  fun getSmart() = MediaItem.Media.Movie(
    backdropPath = "/u0yaNgikY92zkI0t3fiEGZP3UWq.jpg",
    id = 11665,
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
    isFavorite = false,
  )

  fun tvAll() = listOf(
    theWire(),
    theOffice(),
  )

  fun Person() = MediaItem.Person(
    id = 1215572,
    name = "Randall Einhorn",
    profilePath = null,
    knownForDepartment = "Directing",
    gender = Gender.MALE,
  )

  fun the40YearOldVirgin() = MediaItem.Media.Movie(
    backdropPath = "/8GWECkJcBsdReaotUFbl96gAngj.jpg",
    id = 6957,
    name = "The 40 Year Old Virgin",
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
    isFavorite = false,
  )

  fun theIncredibleBurtWonderstone() = MediaItem.Media.Movie(
    backdropPath = "/1Zoti1xJRgKMCuNKnsYLzTyNOOH.jpg",
    id = 124459,
    overview = "After breaking up with his longtime stage partner, " +
      "a famous but jaded Vegas magician fights for relevance " +
      "when a new, \"hip\" street magician appears on the scene.",
    popularity = 18.366,
    posterPath = "/Bl95sg5Ljo3Hu9SSL0JOvLerLh.jpg",
    releaseDate = "2013-03-14",
    name = "The Incredible Burt Wonderstone",
    voteAverage = 5.675,
    voteCount = 1389,
    isFavorite = false,
  )

  fun littleMissSunshine() = MediaItem.Media.Movie(
    backdropPath = "/4YzGG8IPduUQRVnQ0vN5laHA1rH.jpg",
    id = 773,
    overview = "A family loaded with quirky, colorful characters " +
      "piles into an old van and road trips to California for " +
      "little Olive to compete in a beauty pageant.",
    popularity = 21.03,
    posterPath = "/wKn7AJw730emlmzLSmJtzquwaeW.jpg",
    releaseDate = "2006-07-26",
    name = "Little Miss Sunshine",
    voteAverage = 7.689,
    voteCount = 6948,
    isFavorite = false,
  )

  fun despicableMe() = MediaItem.Media.Movie(
    backdropPath = "/2XSeKDmIa2KxaiJy4J9e8FrIZhk.jpg",
    id = 20352,
    overview = "Villainous Gru lives up to his reputation as a despicable," +
      " deplorable and downright unlikable guy when he hatches a plan " +
      "to steal the moon from the sky. But he has a tough time" +
      " staying on task after three orphans land in his care.",
    popularity = 240.938,
    posterPath = "/9lOloREsAhBu0pEtU0BgeR1rHyo.jpg",
    releaseDate = "2010-07-08",
    name = "Despicable Me",
    voteAverage = 7.244,
    voteCount = 14804,
    isFavorite = false,
  )

  fun riot() = MediaItem.Media.TV(
    backdropPath = null,
    id = 60734,
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
    releaseDate = "2014-05-13",
    name = "Riot",
    voteAverage = 1.0,
    voteCount = 1,
    isFavorite = false,
  )

  fun bruceAlmighty() = MediaItem.Media.Movie(
    id = 310,
    posterPath = "/3XJKBKh9Km89EoUEitVTSnrlAkZ.jpg",
    backdropPath = "/8bQbejOvAKe78RXxpkfM04n0K18.jpg",
    overview = "Bruce Nolan toils as a \"human interest\"" +
      " television reporter in Buffalo, NY, " +
      "but despite his high ratings and the love of his" +
      " beautiful girlfriend, Bruce remains unfulfilled." +
      " At the end of the worst day in his life, he angrily " +
      "ridicules God - and the Almighty responds, " +
      "endowing Bruce with all of His divine powers.",
    popularity = 58.75,
    releaseDate = "2003-05-23",
    voteAverage = 6.713,
    voteCount = 10485,
    name = "Bruce Almighty",
    isFavorite = false,
  )

  fun all() = listOf(
    theWire(),
    theOffice(),
    the40YearOldVirgin(),
    theIncredibleBurtWonderstone(),
    riot(),
    getSmart(),
    bruceAlmighty(),
    despicableMe(),
    littleMissSunshine(),

  )

  fun MoviesList(range: IntProgression = 1..10): List<MediaItem.Media.Movie> = range.map {
    MediaItem.Media.Movie(
      id = it,
      backdropPath = "movie $it - backdropPath",
      posterPath = "movie $it - posterPath",
      releaseDate = "2002-08-22",
      name = "Fight club $it",
      voteAverage = (it.toDouble() + 0.5) % 10,
      voteCount = 12_345 + it,
      overview = LoremIpsum(15),
      isFavorite = false,
      popularity = (it * 525.25),
    )
  }

  fun TVList(range: IntProgression = 1..10): List<MediaItem.Media.TV> = range.map {
    MediaItem.Media.TV(
      id = it,
      backdropPath = "tv $it - backdropPath",
      posterPath = "tv $it - posterPath",
      releaseDate = "tv $it - releaseDate",
      name = "tv $it  - name",
      voteAverage = it.toDouble(),
      voteCount = 12_345 + it,
      overview = "overview $it",
      isFavorite = false,
      popularity = (it * 525.50),
    )
  }

  /**
   * Migrate to these values for new tests
   */

  fun moviesPagination() = PaginationData<MediaItem.Media>(
    page = 1,
    totalPages = 3,
    totalResults = 60,
    list = MoviesList(),
  )

  fun tvPagination() = PaginationData<MediaItem.Media>(
    page = 1,
    totalPages = 3,
    totalResults = 60,
    list = tvAll(),
  )

  class MovieMediaItemFactoryWizard(private var mediaItem: MediaItem.Media.Movie) {

    fun withId(id: Int) = apply {
      mediaItem = mediaItem.copy(id = id)
    }

    fun withName(name: String) = apply {
      mediaItem = mediaItem.copy(name = name)
    }

    fun withPosterPath(posterPath: String?) = apply {
      mediaItem = mediaItem.copy(posterPath = posterPath)
    }

    fun withReleaseDate(releaseDate: String) = apply {
      mediaItem = mediaItem.copy(releaseDate = releaseDate)
    }

    fun withVoteAverage(rating: Double) = apply {
      mediaItem = mediaItem.copy(voteAverage = rating)
    }

    fun withVoteCount(voteCount: Int) = apply {
      mediaItem = mediaItem.copy(voteCount = voteCount)
    }

    fun withOverview(overview: String) = apply {
      mediaItem = mediaItem.copy(overview = overview)
    }

    fun withFavorite(isFavorite: Boolean?) = apply {
      mediaItem = mediaItem.copy(isFavorite = isFavorite)
    }

    fun build(): MediaItem.Media.Movie = mediaItem
  }

  class TVMediaItemFactoryWizard(private var mediaItem: MediaItem.Media.TV) {

    fun withId(id: Int) = apply {
      mediaItem = mediaItem.copy(id = id)
    }

    fun withName(name: String) = apply {
      mediaItem = mediaItem.copy(name = name)
    }

    fun withPosterPath(posterPath: String?) = apply {
      mediaItem = mediaItem.copy(posterPath = posterPath)
    }

    fun withReleaseDate(releaseDate: String) = apply {
      mediaItem = mediaItem.copy(releaseDate = releaseDate)
    }

    fun withVoteAverage(rating: Double) = apply {
      mediaItem = mediaItem.copy(voteAverage = rating)
    }

    fun withVoteCount(voteCount: Int) = apply {
      mediaItem = mediaItem.copy(voteCount = voteCount)
    }

    fun withOverview(overview: String) = apply {
      mediaItem = mediaItem.copy(overview = overview)
    }

    fun withFavorite(isFavorite: Boolean) = apply {
      mediaItem = mediaItem.copy(isFavorite = isFavorite)
    }

    fun build(): MediaItem.Media.TV = mediaItem
  }

  fun MediaItem.Media.TV.toWizard(block: TVMediaItemFactoryWizard.() -> Unit) =
    TVMediaItemFactoryWizard(this).apply(block).build()

  fun MediaItem.Media.Movie.toWizard(block: MovieMediaItemFactoryWizard.() -> Unit) =
    MovieMediaItemFactoryWizard(this).apply(block).build()
}
