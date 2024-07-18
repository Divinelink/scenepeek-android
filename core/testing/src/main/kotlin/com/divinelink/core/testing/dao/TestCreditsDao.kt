package com.divinelink.core.testing.dao

import com.divinelink.core.database.credits.dao.CreditsDao
import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestCreditsDao {

  val mock: CreditsDao = mock()

  fun mockCheckIfAggregateCreditsExist(result: Boolean) {
    whenever(
      mock.checkIfAggregateCreditsExist(any()),
    ).thenReturn(
      flowOf(result),
    )
  }

  fun mockFetchAllCredits(credits: AggregateCreditsEntity) {
    whenever(mock.fetchAllCredits(any())).thenReturn(flowOf(credits))
  }
}
