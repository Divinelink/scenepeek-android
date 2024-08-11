package com.divinelink.core.database.person

import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.database.person.credits.PersonCrewCreditEntity
import kotlinx.coroutines.flow.Flow

interface PersonDao {

  fun fetchPersonById(id: Long): Flow<PersonEntity?>

  fun insertPerson(person: PersonEntity)

  fun fetchTopPopularCastCredits(id: Long): Flow<List<PersonCastCreditEntity>>

  fun fetchPersonCombinedCredits(id: Long): Flow<PersonCombinedCreditsEntity?>

  fun insertPersonCredits(id: Long)
  fun insertPersonCastCredits(cast: List<PersonCastCreditEntity>)
  fun insertPersonCrewCredits(crew: List<PersonCrewCreditEntity>)
}
