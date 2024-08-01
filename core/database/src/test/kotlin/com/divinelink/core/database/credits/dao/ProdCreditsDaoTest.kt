package com.divinelink.core.database.credits.dao

import app.cash.turbine.test
import com.divinelink.core.database.Database
import com.divinelink.core.database.credits.cast.SeriesCast
import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.core.commons.ClockFactory
import com.divinelink.core.testing.factories.database.credits.AggregateCreditsFactory
import com.divinelink.core.testing.factories.database.credits.cast.SeriesCastFactory
import com.divinelink.core.testing.factories.database.credits.cast.SeriesCastRoleFactory
import com.divinelink.core.testing.factories.database.credits.crew.SeriesCrewFactory
import com.divinelink.core.testing.factories.database.credits.crew.SeriesCrewJobFactory
import com.divinelink.core.testing.factories.entity.credits.AggregateCreditsEntityFactory
import com.divinelink.core.testing.factories.entity.credits.CastEntityFactory
import com.divinelink.core.testing.factories.entity.credits.CrewEntityFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class ProdCreditsDaoTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var database: Database

  private lateinit var dao: ProdCreditsDao

  private lateinit var clock: Clock

  @Before
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()
    clock = ClockFactory.augustFirst2021()

    dao = ProdCreditsDao(
      database = database,
      clock = clock,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test insert and check if aggregate credit id exists`() = runTest {
    dao.insertAggregateCredits(aggregateCreditsId = 1)

    val result = dao.checkIfAggregateCreditsExist(id = 1).first()

    assertThat(result).isTrue()
  }

  @Test
  fun `test check if aggregate credit exists when is true`() = runTest {
    dao.insertAggregateCredits(aggregateCreditsId = 1)

    val result = dao.checkIfAggregateCreditsExist(id = 1).first()

    assertThat(result).isTrue()
  }

  @Test
  fun `test checkIfExists query when credits are expired`() = runTest {
    clock = ClockFactory.augustFirst2021()

    dao = ProdCreditsDao(
      database = database,
      clock = clock,
      dispatcher = testDispatcher,
    )

    // It should expire 1 month after
    dao.insertAggregateCredits(aggregateCreditsId = 1)

    clock = ClockFactory.septemberSecond2021()

    dao = ProdCreditsDao(
      database = database,
      clock = clock,
      dispatcher = testDispatcher,
    )

    dao.checkIfAggregateCreditsExist(id = 1).test {
      assertThat(awaitItem()).isFalse()
    }
  }

  @Test
  fun `test checkIfExists query when credits are not expired`() = runTest {
    clock = ClockFactory.augustFirst2021()

    dao = ProdCreditsDao(
      database = database,
      clock = clock,
      dispatcher = testDispatcher,
    )

    // It should expire 1 month after
    dao.insertAggregateCredits(aggregateCreditsId = 1)

    clock = ClockFactory.augustFifteenth2021()

    dao = ProdCreditsDao(
      database = database,
      clock = clock,
      dispatcher = testDispatcher,
    )

    dao.checkIfAggregateCreditsExist(id = 1).test {
      assertThat(awaitItem()).isTrue()
    }
  }

  @Test
  fun `test check if aggregate credit exists when is false`() = runTest {
    val result = dao.checkIfAggregateCreditsExist(id = 1).first()

    assertThat(result).isFalse()
  }

  @Test
  fun `test fetchAllCastWithRoles with empty roles does not bring any data`() = runTest {
    dao.insertAggregateCredits(1)

    dao.insertCast(cast = SeriesCastFactory.allCast())

    val result = dao.fetchAllCastWithRoles(id = 1).first()

    assertThat(result).isEqualTo(emptyList<SeriesCast>())
  }

  @Test
  fun `test fetchAllCastWithRoles with roles does successfully gets cast`() = runTest {
    dao.insertAggregateCredits(AggregateCreditsFactory.theOffice().id)

    dao.insertCast(SeriesCastFactory.allCast())
    dao.insertCastRoles(listOf(SeriesCastRoleFactory.kevinMalone()))

    val result = dao.fetchAllCastWithRoles(id = 2316).first()

    assertThat(result).isEqualTo(listOf(CastEntityFactory.brianBaumgartner()))
  }

  @Test
  fun `test fetchAllCrewJobs with empty jobs does not bring any data`() = runTest {
    dao.insertAggregateCredits(1)

    dao.insertCrew(SeriesCrewFactory.cameraDepartment())

    val result = dao.fetchAllCrewJobs(
      aggregateCreditId = AggregateCreditsFactory.theOffice().id,
    ).first()

    assertThat(result).isEqualTo(emptyList<SeriesCrew>())
  }

  @Test
  fun `test fetchAllCrewJobs with jobs does successfully gets crew`() = runTest {
    dao.insertAggregateCredits(AggregateCreditsFactory.theOffice().id)

    dao.insertCrew(SeriesCrewFactory.cameraDepartment())
    dao.insertCrewJobs(SeriesCrewJobFactory.allCrewJobs())

    val result = dao.fetchAllCrewJobs(
      aggregateCreditId = AggregateCreditsFactory.theOffice().id,
    ).first()

    assertThat(result).isEqualTo(CrewEntityFactory.cameraDepartment())
  }

  @Test
  fun `test fetchAllCredits successfully fetches all credits`() = runTest {
    dao.insertAggregateCredits(AggregateCreditsFactory.theOffice().id)

    dao.insertCast(SeriesCastFactory.allCast())
    dao.insertCastRoles(SeriesCastRoleFactory.allCastRoles())

    dao.insertCrew(SeriesCrewFactory.cameraDepartment())
    dao.insertCrewJobs(SeriesCrewJobFactory.allCrewJobs())

    val result = dao.fetchAllCredits(id = AggregateCreditsFactory.theOffice().id).first()

    assertThat(result).isEqualTo(AggregateCreditsEntityFactory.theOffice())
  }

  @Test
  fun `test fetchAllCredits with invalid crew job id does not bring crew`() = runTest {
    dao.insertAggregateCredits(AggregateCreditsFactory.theOffice().id)

    dao.insertCast(SeriesCastFactory.allCast())
    dao.insertCastRoles(SeriesCastRoleFactory.allCastRoles())

    dao.insertCrew(SeriesCrewFactory.cameraDepartment())

    val result = dao.fetchAllCredits(id = AggregateCreditsFactory.theOffice().id).first()

    assertThat(result).isEqualTo(
      AggregateCreditsEntityFactory.theOffice().copy(crew = emptyList()),
    )
  }

  @Test
  fun `test fetchAllCredits with partial valid cast`() = runTest {
    dao.insertAggregateCredits(AggregateCreditsFactory.theOffice().id)

    dao.insertCast(SeriesCastFactory.allCast())
    dao.insertCastRoles(listOf(SeriesCastRoleFactory.kevinMalone()))

    dao.insertCrew(SeriesCrewFactory.cameraDepartment())

    val result = dao.fetchAllCredits(id = AggregateCreditsFactory.theOffice().id).first()

    assertThat(result).isEqualTo(
      AggregateCreditsEntityFactory
        .theOffice()
        .copy(
          cast = listOf(CastEntityFactory.brianBaumgartner()),
          crew = emptyList(),
        ),
    )
  }

  @Test
  fun `test fetchAllCredits with partial valid crew`() = runTest {
    dao.insertAggregateCredits(AggregateCreditsFactory.theOffice().id)

    dao.insertCast(SeriesCastFactory.allCast())
    dao.insertCastRoles(SeriesCastRoleFactory.allCastRoles())

    dao.insertCrew(SeriesCrewFactory.cameraDepartment())
    dao.insertCrewJobs(
      listOf(
        SeriesCrewJobFactory.daleAlexander(),
        SeriesCrewJobFactory.randallEinhorn(),
      ),
    )

    val result = dao.fetchAllCredits(id = AggregateCreditsFactory.theOffice().id).first()

    assertThat(result).isEqualTo(
      AggregateCreditsEntityFactory
        .theOffice()
        .copy(
          cast = CastEntityFactory.allCast(),
          crew = listOf(CrewEntityFactory.daleAlexander(), CrewEntityFactory.randallEinhorn()),
        ),
    )
  }

  @Test
  fun `test fetchAllCredits with invalid cast id does not bring cast`() = runTest {
    dao.insertAggregateCredits(AggregateCreditsFactory.theOffice().id)

    dao.insertCrew(SeriesCrewFactory.cameraDepartment())
    dao.insertCrewJobs(SeriesCrewJobFactory.allCrewJobs())

    val result = dao.fetchAllCredits(id = AggregateCreditsFactory.theOffice().id).first()

    assertThat(result).isEqualTo(
      AggregateCreditsEntityFactory.theOffice().copy(cast = emptyList()),
    )
  }

  @Test
  fun `test fetchCast does not bring data with null character`() = runTest {
    dao.insertAggregateCredits(AggregateCreditsFactory.theOffice().id)

    dao.insertCast(SeriesCastFactory.allCast())
    dao.insertCastRoles(
      listOf(SeriesCastRoleFactory.kevinMalone()),
    )

    val result = dao.fetchAllCastWithRoles(id = AggregateCreditsFactory.theOffice().id).first()

    assertThat(result).isEqualTo(listOf((CastEntityFactory.brianBaumgartner())))
  }
}
