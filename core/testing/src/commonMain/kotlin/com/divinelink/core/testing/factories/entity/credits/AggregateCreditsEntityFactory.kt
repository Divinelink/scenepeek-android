package com.divinelink.core.testing.factories.entity.credits

import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.fixtures.details.person.PersonFactory
import com.divinelink.core.testing.factories.database.credits.AggregateCreditsFactory

object AggregateCreditsEntityFactory {

  fun theOffice() = AggregateCreditsEntity(
    id = AggregateCreditsFactory.theOffice().id,
    cast = listOf(
      PersonFactory.SeriesActor.brianBaumgartner,
      PersonFactory.SeriesActor.angelaKinsey,
    ),
    crew = PersonFactory.cameraDepartment(),
  )
}
