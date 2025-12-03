package com.divinelink.core.database.person.dao

import app.cash.turbine.test
import com.divinelink.core.database.Database
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.database.person.credits.PersonCrewCreditEntity
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.CastCreditsWithMediaFactory
import com.divinelink.core.testing.factories.entity.person.credits.CrewCreditsWithMediaFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCastCreditEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCrewCreditEntityFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock

class ProdPersonDaoTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var clock: Clock
  private lateinit var database: Database
  private lateinit var dao: ProdPersonDao

  @BeforeTest
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()
    clock = ClockFactory.augustFirst2021()

    dao = ProdPersonDao(
      clock = clock,
      database = database,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test fetch person by id`() = runTest {
    val person = PersonEntityFactory.steveCarell()

    dao.insertPerson(person)

    dao.fetchPersonById(id = person.id).test {
      assertThat(awaitItem()).isEqualTo(person)
      expectNoEvents()
    }
  }

  @Test
  fun `test inserting existing person replaces the person`() = runTest {
    val person = PersonEntityFactory.steveCarell()

    dao.insertPerson(person)

    dao.fetchPersonById(id = person.id).test {
      val updatedPerson = person.copy(
        name = "Steve Carell Jr.",
      )

      dao.insertPerson(updatedPerson)
      assertThat(awaitItem()).isEqualTo(person)
      assertThat(awaitItem()).isEqualTo(updatedPerson)
      expectNoEvents()
    }
  }

  @Test
  fun `test fetch person combined credits by id without initial credits`() = runTest {
    val person = PersonEntityFactory.steveCarell()
    val cast = PersonCastCreditEntityFactory.despicableMe()
    val crew = PersonCrewCreditEntityFactory.riot()

    MediaItemFactory.all().forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }

    with(dao) {
      fetchPersonCombinedCredits(id = person.id).test {
        assertThat(awaitItem()).isNull()
        expectNoEvents()

        insertPersonCredits(id = person.id)
        insertPersonCastCredits(listOf(cast))
        insertPersonCrewCredits(listOf(crew))

        val firstEmission = awaitItem()
        assertThat(firstEmission).isNotNull()
        assertThat(firstEmission?.id).isEqualTo(person.id)

        assertThat(awaitItem()?.cast).containsExactly(CastCreditsWithMediaFactory.despicableMe)
        assertThat(awaitItem()?.crew).containsExactly(CrewCreditsWithMediaFactory.riot)
        expectNoEvents()
      }
    }
  }

  @Test
  fun `test fetch person combined credits by id without initial credits and inserted media`() =
    runTest {
      val person = PersonEntityFactory.steveCarell()
      val cast = PersonCastCreditEntityFactory.despicableMe()
      val crew = PersonCrewCreditEntityFactory.riot()

      with(dao) {
        fetchPersonCombinedCredits(id = person.id).test {
          assertThat(awaitItem()).isNull()
          expectNoEvents()

          insertPersonCredits(id = person.id)
          insertPersonCastCredits(listOf(cast))
          insertPersonCrewCredits(listOf(crew))

          val firstEmission = awaitItem()
          assertThat(firstEmission).isNotNull()
          assertThat(firstEmission?.id).isEqualTo(person.id)

          assertThat(awaitItem()?.cast).isEmpty()
          assertThat(awaitItem()?.crew).isEmpty()
          expectNoEvents()
        }
      }
    }

  @Test
  fun `test fetch person combined credits are sorted by date`() = runTest {
    val person = PersonEntityFactory.steveCarell()
    val cast = PersonCastCreditEntityFactory.all()
    val crew = PersonCrewCreditEntityFactory.all()

    MediaItemFactory.all().forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }

    with(dao) {
      insertPersonCredits(id = person.id)
      insertPersonCastCredits(cast)
      insertPersonCrewCredits(crew)

      fetchPersonCombinedCredits(id = person.id).test {
        val result = awaitItem()
        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(person.id)
        assertThat(result?.cast).isEqualTo(CastCreditsWithMediaFactory.sortedByDate)
        assertThat(result?.crew).isEqualTo(CrewCreditsWithMediaFactory.sortedByDate)

        assertThat(result?.cast).isNotEqualTo(cast)
        assertThat(result?.crew).isNotEqualTo(crew)

        expectNoEvents()
      }
    }
  }

  @Test
  fun `test fetch person combined credits without inserted media returns empty`() = runTest {
    val person = PersonEntityFactory.steveCarell()
    val cast = PersonCastCreditEntityFactory.all()
    val crew = PersonCrewCreditEntityFactory.all()

    with(dao) {
      insertPersonCredits(id = person.id)
      insertPersonCastCredits(cast)
      insertPersonCrewCredits(crew)

      fetchPersonCombinedCredits(id = person.id).test {
        val result = awaitItem()
        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(person.id)
        assertThat(result?.cast).isEqualTo(emptyList<PersonCrewCreditEntity>())
        assertThat(result?.crew).isEqualTo(emptyList<PersonCastCreditEntity>())

        expectNoEvents()
      }
    }
  }
}
