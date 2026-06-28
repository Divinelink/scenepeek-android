package com.divinelink.core.fixtures.model.awards

import com.divinelink.core.model.awards.CeremonyCategory

object CeremonyCategoryFactory {

  fun bestPicture() = CeremonyCategory(
    id = "best-picture",
    title = "Best Picture",
  )

  fun bestDirector() = CeremonyCategory(
    id = "best-director",
    title = "Best Director",
  )

  fun all() = listOf(bestPicture(), bestDirector())
}
