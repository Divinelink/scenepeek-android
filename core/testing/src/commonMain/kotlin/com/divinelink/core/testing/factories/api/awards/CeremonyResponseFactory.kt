package com.divinelink.core.testing.factories.api.awards

import com.divinelink.core.network.awards.model.ceremony.CeremoniesResponse
import com.divinelink.core.network.awards.model.ceremony.CeremonyResponse

object CeremonyResponseFactory {

  fun single() = CeremonyResponse(
    id = "oscars",
    title = "Academy Awards",
    imageUrl = "https://example.com/oscars",
  )

  fun all() = CeremoniesResponse(
    ceremonies = listOf(
      single(),
      CeremonyResponse(
        id = "golden-globes",
        title = "Golden Globe Awards",
        imageUrl = "https://example.com/golden-globes",
      ),
    ),
  )
}
