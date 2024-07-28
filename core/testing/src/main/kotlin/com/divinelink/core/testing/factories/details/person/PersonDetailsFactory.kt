package com.divinelink.core.testing.factories.details.person

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.core.testing.factories.entity.person.BIOGRAPHY

object PersonDetailsFactory {

  fun steveCarell() = PersonDetails(
    person = Person(
      id = 4495,
      name = "Steve Carell",
      profilePath = "/dzJtsLspH5Bf8Tvw7OQC47ETNfJ.jpg",
      gender = Gender.MALE,
      role = PersonRole.Unknown,
    ),
    biography = BIOGRAPHY,
    birthday = "1962-08-16",
    deathday = null,
    placeOfBirth = "Concord, Massachusetts, USA",
    homepage = null,
    alsoKnownAs = listOf(
      "Steven Carrel",
      "Steven Carel ",
      "Steve Carel ",
      "Steven Carell",
      "Стив Карелл",
      "ستيف كارل",
      "スティーヴ・カレル",
      "스티브 커렐",
      "สตีฟ คาเรล",
      "史提夫·加維",
      "Steven John \"Steve\" Carell",
      "Steven John Carell",
      "Στίβεν Τζον Καρέλ",
      "Στίβεν Καρέλ",
      "סטיב קארל",
      "Стів Карелл",
    ),
    imdbId = "nm0136797",
    popularity = 77.108,
  )
}
