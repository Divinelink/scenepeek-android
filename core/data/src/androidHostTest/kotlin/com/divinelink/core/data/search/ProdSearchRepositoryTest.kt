package com.divinelink.core.data.search

import app.cash.turbine.test
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.media.dao.ProdMediaDao
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.database.person.mapper.map
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.toStub
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.entity.person.PersonDetailsEntityFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdSearchRepositoryTest {

  private lateinit var mediaDao: MediaDao
  private lateinit var personDao: PersonDao
  private lateinit var repository: ProdSearchRepository

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @BeforeTest
  fun setup() {
    val database = TestDatabaseFactory.createInMemoryDatabase()

    mediaDao = ProdMediaDao(
      database = database,
      dispatcher = testDispatcher,
      clock = ClockFactory.augustFirst2021(),
    )
    personDao = ProdPersonDao(
      clock = ClockFactory.augustFifteenth2021(),
      database = database,
      dispatcher = testDispatcher,
    )
    repository = ProdSearchRepository(
      mediaDao = mediaDao,
      personDao = personDao,
    )
  }

  @Test
  fun `test search history with empty`() = runTest {
    repository.fetchSearchHistory().test {
      awaitItem() shouldBe emptyList()
    }
  }

  @Test
  fun `test search history with items in history but not saved in dao`() = runTest {
    repository.fetchSearchHistory().test {
      awaitItem() shouldBe emptyList()

      repository.addToSearchHistory(media = MediaItemFactory.theOffice().toStub())
      expectNoEvents()

      mediaDao.insertMedia(media = MediaItemFactory.theOffice())
      awaitItem() shouldBe listOf(
        MediaItemFactory.theOffice(),
      )

      mediaDao.insertMedia(media = MediaItemFactory.despicableMe())
      expectNoEvents()

      repository.addToSearchHistory(media = MediaItemFactory.despicableMe().toStub())
      awaitItem() shouldBe listOf(
        MediaItemFactory.theOffice(),
        MediaItemFactory.despicableMe(),
      )

      repository.addToSearchHistory(media = MediaItemFactory.Person().toStub())
      expectNoEvents()

      personDao.insertPerson(person = PersonDetailsEntityFactory.steveCarell())
      expectNoEvents()

      repository.addToSearchHistory(media = PersonDetailsEntityFactory.steveCarell().map().toStub())

      awaitItem() shouldBe listOf(
        MediaItemFactory.theOffice(),
        MediaItemFactory.despicableMe(),
        PersonDetailsEntityFactory.steveCarell().map(),
      )
    }
  }

  @Test
  fun `test search history remove and clear actions`() = runTest {
    mediaDao.insertMedia(media = MediaItemFactory.theOffice())
    mediaDao.insertMedia(media = MediaItemFactory.despicableMe())
    personDao.insertPerson(person = PersonDetailsEntityFactory.steveCarell())

    repository.addToSearchHistory(media = MediaItemFactory.theOffice().toStub())
    repository.addToSearchHistory(media = MediaItemFactory.despicableMe().toStub())
    repository.addToSearchHistory(media = MediaItemFactory.steveCarell.toStub())

    repository.fetchSearchHistory().test {
      awaitItem() shouldBe listOf(
        MediaItemFactory.theOffice(),
        MediaItemFactory.despicableMe(),
        MediaItemFactory.steveCarell,
      )

      repository.removeFromHistory(MediaItemFactory.despicableMe().toStub())
      awaitItem() shouldBe listOf(
        MediaItemFactory.theOffice(),
        MediaItemFactory.steveCarell,
      )

      mediaDao.clearSearchHistory()

      awaitItem() shouldBe emptyList()
    }
  }
}
