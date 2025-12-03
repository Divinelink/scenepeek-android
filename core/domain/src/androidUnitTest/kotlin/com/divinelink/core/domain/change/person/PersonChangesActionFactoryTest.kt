package com.divinelink.core.domain.change.person

import com.divinelink.core.model.change.ChangeType
import com.divinelink.core.testing.repository.TestPersonRepository
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class PersonChangesActionFactoryTest {

  private val repository = TestPersonRepository()

  @Test
  fun `test getAction with biography`() {
    val factory = PersonChangesActionFactory(repository.mock)

    val action = factory.getAction(ChangeType.BIOGRAPHY)

    assertThat(action).isInstanceOf(UpdateBiography::class.java)
  }

  @Test
  fun `test getAction with name`() {
    val factory = PersonChangesActionFactory(repository.mock)

    val action = factory.getAction(ChangeType.NAME)

    assertThat(action).isInstanceOf(UpdateName::class.java)
  }

  @Test
  fun `test getAction with imdb id`() {
    val factory = PersonChangesActionFactory(repository.mock)

    val action = factory.getAction(ChangeType.IMDB_ID)

    assertThat(action).isInstanceOf(UpdateImdbID::class.java)
  }

  @Test
  fun `test getAction with birthday`() {
    val factory = PersonChangesActionFactory(repository.mock)

    val action = factory.getAction(ChangeType.BIRTHDAY)

    assertThat(action).isInstanceOf(UpdateBirthday::class.java)
  }

  @Test
  fun `test getAction with day of death`() {
    val factory = PersonChangesActionFactory(repository.mock)

    val action = factory.getAction(ChangeType.DAY_OF_DEATH)

    assertThat(action).isInstanceOf(UpdateDayOfDeath::class.java)
  }

  @Test
  fun `test getAction with gender`() {
    val factory = PersonChangesActionFactory(repository.mock)

    val action = factory.getAction(ChangeType.GENDER)

    assertThat(action).isInstanceOf(UpdateGender::class.java)
  }

  @Test
  fun `test getAction with place of birth`() {
    val factory = PersonChangesActionFactory(repository.mock)

    val action = factory.getAction(ChangeType.PLACE_OF_BIRTH)

    assertThat(action).isInstanceOf(UpdatePlaceOfBirth::class.java)
  }

  @Test
  fun `test getAction with homepage`() {
    val factory = PersonChangesActionFactory(repository.mock)

    val action = factory.getAction(ChangeType.HOMEPAGE)

    assertThat(action).isInstanceOf(UpdateHomepage::class.java)
  }
}
