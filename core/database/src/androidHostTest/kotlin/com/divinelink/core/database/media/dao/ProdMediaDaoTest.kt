package com.divinelink.core.database.media.dao

import app.cash.turbine.test
import com.divinelink.core.database.Database
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.fixtures.model.GenreFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.toStub
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class ProdMediaDaoTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var database: Database
  private lateinit var dao: ProdMediaDao

  @Before
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()

    dao = ProdMediaDao(
      database = database,
      dispatcher = testDispatcher,
      clock = ClockFactory.augustFirst2021(),
    )
  }

  @Test
  fun `test insert and fetch media items`() {
    val item = dao.fetchMedia(MediaItemFactory.theWire().toStub())

    item shouldBe null

    dao.insertMedia(MediaItemFactory.theWire())

    val fetchedItem = dao.fetchMedia(MediaItemFactory.theWire().toStub())

    fetchedItem shouldBe MediaItemFactory.theWire()
  }

  @Test
  fun `test insert list of items`() {
    dao.fetchMedia(MediaItemFactory.theWire().toStub()) shouldBe null
    dao.fetchMedia(MediaItemFactory.theOffice().toStub()) shouldBe null

    dao.insertMediaList(MediaItemFactory.tvAll())

    dao.fetchMedia(MediaItemFactory.theWire().toStub()) shouldBe MediaItemFactory.theWire()
    dao.fetchMedia(MediaItemFactory.theOffice().toStub()) shouldBe MediaItemFactory.theOffice()
  }

  @Test
  fun `test insert seasons`() = runTest {
    val media = MediaItemFactory.theOffice()

    dao.fetchSeasons(media.id).test {
      awaitItem() shouldBe emptyList()

      dao.insertSeasons(
        id = media.id,
        seasons = SeasonFactory.all(),
      )

      awaitItem() shouldBe SeasonFactory.all()

      dao.insertSeasons(
        id = media.id,
        seasons = SeasonFactory.allWithStatus(),
      )

      awaitItem() shouldBe SeasonFactory.allWithStatus().map { season ->
        if (season.seasonNumber == 9) {
          season.copy(status = null)
        } else {
          season
        }
      }
    }
  }

  @Test
  fun `test updateSeasonStatus with override false does not clear seasons`() = runTest {
    val media = MediaItemFactory.theOffice()
    val seasons = SeasonFactory.all()
    val seasonsToBeUpdated = SeasonFactory.allWithStatus().subList(3, 7)
    dao.insertSeasons(id = media.id, seasons = seasons)

    dao.fetchSeasons(media.id).test {
      awaitItem() shouldContainAll seasons

      dao.updateSeasonStatus(
        mediaId = media.id,
        seasons = seasonsToBeUpdated.map {
          SeasonRequest(
            it.seasonNumber,
            it.status ?: JellyseerrStatus.Media.UNKNOWN,
          )
        },
        override = false,
      )

      val updatedSeasons = SeasonFactory.all().map { season ->
        season.copy(
          status = when (season.seasonNumber) {
            3 -> JellyseerrStatus.Media.AVAILABLE
            4 -> JellyseerrStatus.Media.AVAILABLE
            5 -> JellyseerrStatus.Media.AVAILABLE
            6 -> JellyseerrStatus.Media.AVAILABLE
            else -> null
          },
        )
      }

      awaitItem() shouldBe updatedSeasons
    }
  }

  @Test
  fun `test updateSeasonStatus with override true clears seasons`() = runTest {
    val media = MediaItemFactory.theOffice()
    val seasons = SeasonFactory.all()
    val seasonsToBeUpdated = SeasonFactory.allWithStatus().subList(3, 7)
    dao.insertSeasons(id = media.id, seasons = seasons)

    dao.fetchSeasons(media.id).test {
      awaitItem() shouldContainAll seasons

      dao.updateSeasonStatus(
        mediaId = media.id,
        seasons = seasonsToBeUpdated.map {
          SeasonRequest(
            it.seasonNumber,
            it.status ?: JellyseerrStatus.Media.UNKNOWN,
          )
        },
        override = true,
      )

      awaitItem() shouldBe SeasonFactory.all().map { season ->
        season.copy(
          status = when (season.seasonNumber) {
            3 -> JellyseerrStatus.Media.AVAILABLE
            4 -> JellyseerrStatus.Media.AVAILABLE
            5 -> JellyseerrStatus.Media.AVAILABLE
            6 -> JellyseerrStatus.Media.AVAILABLE
            else -> season.status
          },
        )
      }
    }
  }

  @Test
  fun `test fetch and insert genres for movies`() = runTest {
    dao.fetchGenres(mediaType = MediaType.MOVIE).test {
      awaitItem() shouldBe emptyList()
      dao.insertGenres(mediaType = MediaType.MOVIE, genres = GenreFactory.Movie.all)
      awaitItem() shouldBe GenreFactory.Movie.all.sortedBy { it.name }
    }
  }

  @Test
  fun `test fetch and insert genres for tv`() = runTest {
    dao.fetchGenres(mediaType = MediaType.TV).test {
      awaitItem() shouldBe emptyList()
      dao.insertGenres(mediaType = MediaType.TV, genres = GenreFactory.Tv.all)
      awaitItem() shouldBe GenreFactory.Tv.all.sortedBy { it.name }
    }
  }

  @Test
  fun `test fetch movie favorites`() = runTest {
    dao.fetchFavorites(mediaType = MediaType.MOVIE).test {
      awaitItem() shouldBe emptyList()
    }

    dao.insertMediaList(media = MediaItemFactory.all())
    MediaItemFactory.all().forEach {
      dao.addToFavorites(
        mediaId = it.id,
        mediaType = it.mediaType,
      )
    }

    dao.fetchFavorites(mediaType = MediaType.MOVIE).test {
      awaitItem() shouldBe MediaItemFactory.movies().map {
        it.copy(isFavorite = true)
      }
    }
  }

  @Test
  fun `test fetch tv favorites`() = runTest {
    dao.insertMediaList(media = MediaItemFactory.all())

    dao.fetchFavorites(mediaType = MediaType.TV).test {
      awaitItem() shouldBe emptyList()

      MediaItemFactory.all().forEach {
        dao.addToFavorites(
          mediaId = it.id,
          mediaType = it.mediaType,
        )
      }

      awaitItem() shouldBe listOf(
        MediaItemFactory.theWire().copy(isFavorite = true),
      )
      awaitItem() shouldBe listOf(
        MediaItemFactory.theWire().copy(isFavorite = true),
        MediaItemFactory.theOffice().copy(isFavorite = true),
      )
      awaitItem() shouldBe listOf(
        MediaItemFactory.theWire().copy(isFavorite = true),
        MediaItemFactory.theOffice().copy(isFavorite = true),
        MediaItemFactory.riot().copy(isFavorite = true),
      )
    }
  }
}
