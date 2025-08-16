package com.divinelink.core.data.person.repository

import app.cash.sqldelight.db.QueryResult
import com.divinelink.core.database.person.PersonChangeField
import com.divinelink.core.model.change.Changes
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import com.divinelink.core.network.Resource
import com.divinelink.core.network.media.model.changes.ChangesParameters
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

  fun fetchPersonDetails(id: Long): Flow<Result<PersonDetails>>

  fun fetchPersonCredits(id: Long): Flow<Resource<PersonCombinedCredits?>>

  fun fetchPersonChanges(
    id: Long,
    params: ChangesParameters,
  ): Flow<Result<Changes>>

  fun updatePerson(
    id: Long,
    biography: String? = null,
    name: String? = null,
    birthday: String? = null,
    deathday: String? = null,
    gender: Int? = null,
    homepage: String? = null,
    imdbId: String? = null,
    knownForDepartment: String? = null,
    placeOfBirth: String? = null,
    profilePath: String? = null,
    insertedAt: String? = null,
  ): QueryResult<Long>

  fun deleteFromPerson(
    id: Long,
    field: PersonChangeField,
  ): QueryResult<Long>
}
