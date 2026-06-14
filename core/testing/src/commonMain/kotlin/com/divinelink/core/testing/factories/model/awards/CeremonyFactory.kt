package com.divinelink.core.testing.factories.model.awards

import com.divinelink.core.model.awards.Ceremony

object CeremonyFactory {

  fun oscars() = Ceremony(
    id = "oscars",
    title = "Academy Awards",
    url = "https://example.com/oscars",
  )

  fun goldenGlobes() = Ceremony(
    id = "golden-globes",
    title = "Golden Globe Awards",
    url = "https://example.com/golden-globes",
  )

  fun all() = listOf(oscars(), goldenGlobes())
}
