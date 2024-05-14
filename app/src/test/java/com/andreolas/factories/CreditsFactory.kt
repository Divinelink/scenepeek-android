package com.andreolas.factories

import com.divinelink.core.network.media.model.details.Credits

object CreditsFactory {

  fun all() = Credits(
    cast = CastFactory.all(),
    crew = CrewFactory.all(),
  )
}
