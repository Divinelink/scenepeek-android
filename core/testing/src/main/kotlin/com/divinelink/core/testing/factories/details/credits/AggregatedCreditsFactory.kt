package com.divinelink.core.testing.factories.details.credits

import com.divinelink.core.fixtures.details.credits.SeriesCastFactory
import com.divinelink.core.fixtures.details.credits.SeriesCrewListFactory
import com.divinelink.core.model.credits.AggregateCredits

object AggregatedCreditsFactory {

  fun credits() = AggregateCredits(
    id = 2316,
    cast = SeriesCastFactory.cast(),
    crewDepartments = SeriesCrewListFactory.crewDepartments(),
  )

  // Data fetched from the API is unsorted
  fun unsortedCredits() = AggregateCredits(
    id = 2316,
    cast = SeriesCastFactory.cast().take(2),
    crewDepartments = listOf(
      SeriesCrewListFactory.unsortedCameraDepartment(),
    ),
  )

  // Data fetched from the database is sorted by name
  fun partialCredits() = AggregateCredits(
    id = 2316,
    cast = SeriesCastFactory.cast().take(2),
    crewDepartments = listOf(SeriesCrewListFactory.sortedCameraDepartment()),
  )
}
