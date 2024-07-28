package com.divinelink.core.testing.factories.api.details.person

import com.divinelink.core.network.details.person.model.PersonDetailsApi
import com.divinelink.core.testing.factories.entity.person.BIOGRAPHY

object PersonDetailsApiFactory {

  fun steveCarell() = PersonDetailsApi(
    id = 4495,
    biography = BIOGRAPHY,
    birthday = "1962-08-16",
    deathday = null,
    gender = 2,
    homepage = null,
    imdbId = "nm0136797",
    knownForDepartment = "Acting",
    name = "Steve Carell",
    placeOfBirth = "Concord, Massachusetts, USA",
    popularity = 77.108,
    profilePath = "/dzJtsLspH5Bf8Tvw7OQC47ETNfJ.jpg",
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
  )
}
