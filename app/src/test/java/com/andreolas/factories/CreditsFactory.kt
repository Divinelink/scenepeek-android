package com.andreolas.factories

import com.divinelink.core.network.movies.model.details.Credits

object CreditsFactory {

  fun all() = Credits(
    cast = CastFactory.all(),
    crew = CrewFactory.all(),
  )
}
