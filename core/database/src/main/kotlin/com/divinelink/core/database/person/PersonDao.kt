package com.divinelink.core.database.person

import app.cash.sqldelight.db.QueryResult
import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.database.person.credits.PersonCrewCreditEntity
import kotlinx.coroutines.flow.Flow

interface PersonDao {

  fun fetchPersonById(id: Long): Flow<PersonEntity?>

  fun insertPerson(person: PersonEntity): QueryResult<Long>

  fun updatePerson(
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
    id: Long,
  ): QueryResult<Long>

  fun deleteFromPerson(
    id: Long,
    field: PersonChangeField,
  ): QueryResult<Long>

  fun fetchTopPopularCastCredits(id: Long): Flow<List<PersonCastCreditEntity>>

  fun fetchPersonCombinedCredits(id: Long): Flow<PersonCombinedCreditsEntity?>

  fun insertPersonCredits(id: Long)
  fun insertPersonCastCredits(cast: List<PersonCastCreditEntity>)
  fun insertPersonCrewCredits(crew: List<PersonCrewCreditEntity>)
}
