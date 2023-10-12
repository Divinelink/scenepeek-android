package com.andreolas.movierama.factories

import com.andreolas.movierama.base.data.remote.movies.dto.details.Credits

object CreditsFactory {

  fun all() = Credits(
    cast = CastFactory.all(),
    crew = CrewFactory.all(),
  )
}
