package com.divinelink.factories

import com.divinelink.core.network.media.model.details.CreditsApi

object CreditsFactory {

  fun all() = CreditsApi(
    cast = CastFactory.all(),
    crew = CrewFactory.all(),
  )
}
