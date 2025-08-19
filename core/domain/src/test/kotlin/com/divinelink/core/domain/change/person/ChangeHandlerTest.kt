package com.divinelink.core.domain.change.person

import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.data.person.repository.ProdPersonRepository
import com.divinelink.core.database.Database
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.model.person.Gender
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.dao.TestMediaDao
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
import com.divinelink.core.testing.factories.model.change.PersonChangeItemSample
import com.divinelink.core.testing.factories.model.change.PersonChangeItemSample.toWizard
import com.divinelink.core.testing.service.TestPersonService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock

class ChangeHandlerTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: PersonRepository

  private val personDetails = PersonDetailsFactory.steveCarell()
  private val person = personDetails.person

  private lateinit var clock: Clock
  private lateinit var database: Database
  private lateinit var dao: ProdPersonDao
  private lateinit var mediaDao: TestMediaDao

  @BeforeTest
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()
    clock = ClockFactory.augustFirst2021()
    dao = ProdPersonDao(
      clock = clock,
      database = database,
      dispatcher = testDispatcher,
    )
    mediaDao = TestMediaDao()
    dao.insertPerson(PersonEntityFactory.empty())

    repository = ProdPersonRepository(
      service = TestPersonService().mock,
      dao = dao,
      clock = clock,
      dispatcher = testDispatcher,
      mediaDao = mediaDao.mock,
    )
  }

  @Test
  fun `test UpdateBiography ChangeHandler`() = runTest {
    repository.fetchPersonDetails(person.id).test {
      awaitItem()
      val updateBiography = UpdateBiography(repository)

      updateBiography.execute(
        id = person.id,
        items = PersonChangeItemSample.biography(),
      )

      val result = awaitItem().data

      assertThat(result.biography).isEqualTo(
        "Alexandros Karpas is a Greek YouTuber, filmmaker," +
          " and co-founder of the multimedia company Unboxholics",
      )
    }
  }

  @Test
  fun `test UpdateBiography ChangeHandler with delete action`() = runTest {
    repository.updatePerson(
      id = person.id,
      biography = "Alexandros Karpas is a Greek YouTuber, filmmaker," +
        " and co-founder of the multimedia company Unboxholics",
    )

    repository.fetchPersonDetails(person.id).test {
      awaitItem()
      val updateBiography = UpdateBiography(repository)

      updateBiography.execute(
        id = person.id,
        items = PersonChangeItemSample.biography().toWizard { withDeleteAction() },
      )

      val result = awaitItem().data

      assertThat(result.biography).isEqualTo(null)
    }
  }

  @Test
  fun `test UpdateGender ChangeHandler`() = runTest {
    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.person.gender).isEqualTo(Gender.NOT_SET)
      val updateGender = UpdateGender(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.gender(),
      )

      val result = awaitItem().data

      assertThat(result.person.gender).isEqualTo(Gender.MALE)
    }
  }

  @Test
  fun `test UpdateGender ChangeHandler with delete action`() = runTest {
    repository.updatePerson(
      id = person.id,
      gender = 2,
    )

    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.person.gender).isEqualTo(Gender.MALE)
      val updateGender = UpdateGender(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.gender().toWizard { withDeleteAction() },
      )

      val result = awaitItem().data

      assertThat(result.person.gender).isEqualTo(Gender.NOT_SET)
    }
  }

  @Test
  fun `test UpdateBirthday ChangeHandler`() = runTest {
    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.birthday).isEqualTo(null)
      val updateGender = UpdateBirthday(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.birthday(),
      )

      assertThat(awaitItem().data.birthday).isEqualTo("1986-12-04")
    }
  }

  @Test
  fun `test UpdateBirthday ChangeHandler with delete action`() = runTest {
    repository.updatePerson(
      id = person.id,
      birthday = "1986-12-04",
    )

    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.birthday).isEqualTo("1986-12-04")
      val updateGender = UpdateBirthday(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.birthday().toWizard { withDeleteAction() },
      )

      assertThat(awaitItem().data.birthday).isEqualTo(null)
    }
  }

  @Test
  fun `test UpdateDeathday ChangeHandler with update action`() = runTest {
    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.deathday).isEqualTo(null)
      val updateGender = UpdateDayOfDeath(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.deathday(),
      )

      assertThat(awaitItem().data.deathday).isEqualTo("2024-09-10")
    }
  }

  @Test
  fun `test UpdateDeathday ChangeHandler with delete action`() = runTest {
    repository.updatePerson(
      id = person.id,
      deathday = "2024-09-10",
    )

    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.deathday).isEqualTo("2024-09-10")
      val updateGender = UpdateDayOfDeath(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.deathday().toWizard { withDeleteAction() },
      )

      assertThat(awaitItem().data.deathday).isEqualTo(null)
    }
  }

  @Test
  fun `test UpdateHomepage ChangeHandler with update action`() = runTest {
    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.homepage).isEqualTo(null)
      val updateGender = UpdateHomepage(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.homepage(),
      )

      assertThat(awaitItem().data.homepage).isEqualTo("https://www.unboxholics.com")
    }
  }

  @Test
  fun `test UpdateHomepage ChangeHandler with delete action`() = runTest {
    repository.updatePerson(
      id = person.id,
      homepage = "https://www.unboxholics.com",
      name = "Alexandros Karpas",
    )

    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.homepage).isEqualTo("https://www.unboxholics.com")
      val updateGender = UpdateHomepage(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.homepage().toWizard { withDeleteAction() },
      )

      val result = awaitItem().data
      assertThat(result.homepage).isEqualTo(null)
      assertThat(result.person.name).isEqualTo("Alexandros Karpas")
    }
  }

  @Test
  fun `test UpdateImdbID ChangeHandler`() = runTest {
    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.imdbId).isEqualTo(null)
      val updateGender = UpdateImdbID(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.imdbId(),
      )

      val result = awaitItem().data
      assertThat(result.imdbId).isEqualTo("nm001")
    }
  }

  @Test
  fun `test UpdateImdbID ChangeHandler with delete action`() = runTest {
    repository.updatePerson(
      id = person.id,
      imdbId = "nm000",
    )

    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.imdbId).isEqualTo("nm000")
      val updateGender = UpdateImdbID(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.imdbId().toWizard { withDeleteAction() },
      )

      val result = awaitItem().data
      assertThat(result.imdbId).isEqualTo(null)
    }
  }

  @Test
  fun `test UpdateName ChangeHandler`() = runTest {
    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.person.name).isEqualTo("")
      val updateGender = UpdateName(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.name(),
      )

      val result = awaitItem().data
      assertThat(result.person.name).isEqualTo("Alexandros Karpas")
    }
  }

  @Test
  fun `test UpdateName ChangeHandler with delete action`() = runTest {
    repository.updatePerson(
      id = person.id,
      name = "Alexandros Karpas",
    )

    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.person.name).isEqualTo("Alexandros Karpas")
      val updateGender = UpdateName(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.imdbId().toWizard { withDeleteAction() },
      )

      val result = awaitItem().data
      assertThat(result.person.name).isEqualTo("")
    }
  }

  @Test
  fun `test UpdatePlaceOfBirth ChangeHandler`() = runTest {
    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.placeOfBirth).isEqualTo(null)
      val updateGender = UpdatePlaceOfBirth(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.placeOfBirth(),
      )

      val result = awaitItem().data
      assertThat(result.placeOfBirth).isEqualTo("Aridaia, Greece")
    }
  }

  @Test
  fun `test UpdatePlaceOfBirth ChangeHandler with delete action`() = runTest {
    repository.updatePerson(
      id = person.id,
      placeOfBirth = "Aridaia, Greece",
    )

    repository.fetchPersonDetails(person.id).test {
      assertThat(awaitItem().data.placeOfBirth).isEqualTo("Aridaia, Greece")
      val updateGender = UpdatePlaceOfBirth(repository)

      updateGender.execute(
        id = person.id,
        items = PersonChangeItemSample.placeOfBirth().toWizard { withDeleteAction() },
      )

      val result = awaitItem().data
      assertThat(result.placeOfBirth).isEqualTo(null)
    }
  }
}
