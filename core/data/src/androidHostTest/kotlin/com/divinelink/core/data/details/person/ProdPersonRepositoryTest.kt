package com.divinelink.core.data.details.person

import JvmUnitTestDemoAssetManager
import app.cash.turbine.test
import com.divinelink.core.commons.data
import com.divinelink.core.data.person.details.mapper.map
import com.divinelink.core.data.person.repository.ProdPersonRepository
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.media.dao.ProdMediaDao
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.bruceAlmighty
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.despicableMe
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.littleMissSunshine
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.theOffice
import com.divinelink.core.fixtures.model.person.credit.PersonCombinedCreditsFactory
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import com.divinelink.core.network.Resource
import com.divinelink.core.network.changes.model.api.ChangesResponseApi
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.details.person.model.PersonCreditsApi
import com.divinelink.core.network.media.model.changes.ChangesParameters
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.api.details.person.PersonDetailsApiFactory
import com.divinelink.core.testing.factories.entity.person.PersonDetailsEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCastCreditEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCrewCreditEntityFactory
import com.divinelink.core.testing.factories.model.change.ChangeSample
import com.divinelink.core.testing.service.TestPersonService
import com.google.common.truth.Truth.assertThat
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock

class ProdPersonRepositoryTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: ProdPersonRepository
  private lateinit var service: TestPersonService
  private lateinit var dao: PersonDao
  private lateinit var mediaDao: MediaDao
  private lateinit var clock: Clock

  @BeforeTest
  fun setUp() {
    val database = TestDatabaseFactory.createInMemoryDatabase()

    dao = ProdPersonDao(
      clock = ClockFactory.augustFifteenth2021(),
      database = database,
      dispatcher = testDispatcher,
    )
    service = TestPersonService()
    mediaDao = ProdMediaDao(
      clock = ClockFactory.augustFifteenth2021(),
      database = database,
      dispatcher = testDispatcher,
    )
    clock = ClockFactory.augustFifteenth2021()

    repository = ProdPersonRepository(
      service = service.mock,
      dao = dao,
      clock = clock,
      mediaDao = mediaDao,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test fetchPersonDetails with local data only fetches from database`() = runTest {
    dao.insertPerson(PersonDetailsEntityFactory.steveCarell())

    repository.fetchPersonDetails(id = 4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(PersonDetailsEntityFactory.steveCarell().map())
      expectNoEvents()
    }
  }

  @Test
  fun `test fetchPersonDetails without cache fetches from service and saves to db`() = runTest {
    service.mockFetchPersonDetails(response = PersonDetailsApiFactory.steveCarell())

    repository.fetchPersonDetails(id = 4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(PersonDetailsEntityFactory.steveCarell().map())
      expectNoEvents()
    }
  }

  @Test
  fun `test fetchPersonCredits with local data only fetches from database`() = runTest {
    dao.insertPersonCredits(id = 4495)
    dao.insertPersonCastCredits(cast = PersonCastCreditEntityFactory.all())
    dao.insertPersonCrewCredits(crew = PersonCrewCreditEntityFactory.sortedByDate())

    PersonCombinedCreditsFactory.all().crew.forEach {
      mediaDao.insertMedia(it.media)
    }

    PersonCombinedCreditsFactory.all().cast.forEach {
      mediaDao.insertMedia(it.media)
    }

    repository.fetchPersonCredits(id = 4495).test {
      awaitItem() shouldBe Resource.Loading(PersonCombinedCreditsFactory.sortedByDate())
      awaitItem() shouldBe Resource.Success(PersonCombinedCreditsFactory.sortedByDate())
    }
  }

  @Test
  fun `test fetchPersonCredits with favorite media`() = runTest {
    dao.insertPersonCredits(id = 4495)
    dao.insertPersonCastCredits(cast = PersonCastCreditEntityFactory.all())
    dao.insertPersonCrewCredits(crew = PersonCrewCreditEntityFactory.sortedByDate())

    PersonCombinedCreditsFactory.all().crew.forEach {
      mediaDao.insertMedia(it.media)
    }

    PersonCombinedCreditsFactory.all().cast.forEach {
      mediaDao.insertMedia(it.media)
    }

    repository.fetchPersonCredits(id = 4495).test {
      awaitItem() shouldBe Resource.Loading(PersonCombinedCreditsFactory.sortedByDate())
      awaitItem() shouldBe Resource.Success(PersonCombinedCreditsFactory.sortedByDate())

      mediaDao.addToFavorites(
        MediaItemFactory.theOffice().id,
        MediaItemFactory.theOffice().mediaType,
      )

      awaitItem() shouldBe Resource.Success(
        PersonCombinedCreditsFactory.sortedByDate().copy(
          cast = listOf(
            despicableMe(),
            littleMissSunshine(),
            theOffice().copy(
              media = MediaItemFactory.theOffice().copy(isFavorite = true),
            ),
            bruceAlmighty(),
          ),
        ),
      )

      awaitItem() shouldBe Resource.Success(
        PersonCombinedCreditsFactory.sortedByDate().copy(
          cast = listOf(
            despicableMe(),
            littleMissSunshine(),
            theOffice().copy(
              media = MediaItemFactory.theOffice().copy(isFavorite = true),
            ),
            bruceAlmighty(),
          ),
        ),
      )

      mediaDao.addToFavorites(
        MediaItemFactory.despicableMe().id,
        MediaItemFactory.despicableMe().mediaType,
      )

      awaitItem() shouldBe Resource.Success(
        PersonCombinedCreditsFactory.sortedByDate().copy(
          cast = listOf(
            despicableMe().copy(
              media = MediaItemFactory.despicableMe().copy(isFavorite = false),
            ),
            littleMissSunshine(),
            theOffice().copy(
              media = MediaItemFactory.theOffice().copy(isFavorite = true),
            ),
            bruceAlmighty(),
          ),
        ),
      )

      awaitItem() shouldBe Resource.Success(
        PersonCombinedCreditsFactory.sortedByDate().copy(
          cast = listOf(
            despicableMe().copy(
              media = MediaItemFactory.despicableMe().copy(isFavorite = true),
            ),
            littleMissSunshine(),
            theOffice().copy(
              media = MediaItemFactory.theOffice().copy(isFavorite = true),
            ),
            bruceAlmighty(),
          ),
        ),
      )
    }
  }

  @Test
  fun `test fetchPersonCredits without cache fetches from service and saves to db`() = runTest {
    JvmUnitTestDemoAssetManager.open("person-combined-credits.json").use {
      val personDetails = it.readBytes().decodeToString().trimIndent()

      val serializer = PersonCreditsApi.serializer()
      val personCreditsApi = localJson.decodeFromString(serializer, personDetails)

      service.mockFetchPersonCombinedCredits(response = personCreditsApi)

      repository.fetchPersonCredits(id = 4495).test {
        assertThat(awaitItem()).isEqualTo(Resource.Loading(null))
        val secondEmission = awaitItem() as Resource.Success<PersonCombinedCredits?>
        assertThat(secondEmission).isInstanceOf(Resource.Success::class.java)
        assertThat(secondEmission.data?.cast?.size).isEqualTo(123)
        assertThat(secondEmission.data?.crew?.size).isEqualTo(17)
        expectNoEvents()
      }
    }
  }

  @Test
  fun `test fetchPersonChanges`() = runTest {
    JvmUnitTestDemoAssetManager.open("changes-person.json").use {
      val changes = it.readBytes().decodeToString().trimIndent()

      val serializer = ChangesResponseApi.serializer()
      val changesResponseApi = localJson.decodeFromString(serializer, changes)

      service.mockFetchPersonChanges(response = changesResponseApi)

      repository.fetchPersonChanges(
        id = 4495,
        params = ChangesParameters(
          page = 1,
          startDate = "2021-08-01",
          endDate = "2021-08-15",
        ),
      ).test {
        assertThat(awaitItem().data).isEqualTo(ChangeSample.changes())
        awaitComplete()
      }
    }
  }
}
