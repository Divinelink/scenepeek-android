package com.divinelink.core.database.person

import app.cash.sqldelight.db.QueryResult
import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.database.person.credits.PersonCrewCreditEntity
import com.divinelink.core.model.details.Person
import kotlinx.coroutines.flow.Flow

interface PersonDao {

  fun fetchPersonById(id: Long): Flow<PersonDetailsEntity?>

  fun insertPerson(person: PersonDetailsEntity): QueryResult<Long>

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

  fun fetchPersonCombinedCredits(id: Long): Flow<PersonCombinedCreditsEntity?>

  fun insertPersonCredits(id: Long)
  fun insertPersonCastCredits(cast: List<PersonCastCreditEntity>)
  fun insertPersonCrewCredits(crew: List<PersonCrewCreditEntity>)

  fun insertGuestStars(
    showId: Int,
    season: Int,
    episode: Int,
    guestStars: List<Person>,
  )

  fun fetchGuestStars(
    showId: Int,
    season: Int,
  ): Flow<List<Person>>

  fun fetchEpisodeGuestStars(
    showId: Int,
    season: Int,
    episode: Int,
  ): Flow<List<Person>>
}
