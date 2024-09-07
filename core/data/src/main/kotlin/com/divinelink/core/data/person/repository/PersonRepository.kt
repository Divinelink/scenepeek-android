package com.divinelink.core.data.person.repository

import com.divinelink.core.model.change.Change
import com.divinelink.core.model.change.Changes
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import com.divinelink.core.network.media.model.changes.ChangesParameters
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

  fun fetchPersonDetails(id: Long): Flow<Result<PersonDetails>>

  fun fetchPersonCredits(id: Long): Flow<Result<PersonCombinedCredits>>

  fun fetchPersonChanges(
    id: Long,
    params: ChangesParameters,
  ): Flow<Result<Changes>>

  /**
   * Only apply changes with the given locale. Discard the rest.
   */
  fun applyChanges(
    id: Long,
    changes: List<Change>,
  )
}
