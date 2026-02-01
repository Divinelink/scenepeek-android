package com.divinelink.core.database.person

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.cast.PersonEntity
import com.divinelink.core.database.cast.PersonRoleEntity
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.database.person.credits.CastCreditsWithMedia
import com.divinelink.core.database.person.credits.CrewCreditsWithMedia
import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.database.person.credits.PersonCreditsEntity
import com.divinelink.core.database.person.credits.PersonCrewCreditEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class ProdPersonDao(
  private val clock: Clock,
  private val database: Database,
  private val dispatcher: DispatcherProvider,
) : PersonDao {

  override fun fetchPersonById(id: Long): Flow<PersonDetailsEntity?> = database
    .personDetailsEntityQueries
    .fetchPersonById(id)
    .asFlow()
    .mapToOneOrNull(context = dispatcher.io)

  override fun insertPerson(person: PersonDetailsEntity) = database
    .personDetailsEntityQueries
    .insertPerson(person)

  override fun updatePerson(
    biography: String?,
    name: String?,
    birthday: String?,
    deathday: String?,
    gender: Int?,
    homepage: String?,
    imdbId: String?,
    knownForDepartment: String?,
    placeOfBirth: String?,
    profilePath: String?,
    insertedAt: String?,
    id: Long,
  ) = database
    .personDetailsEntityQueries
    .updatePerson(
      biography = biography,
      name = name,
      birthday = birthday,
      deathday = deathday,
      gender = gender?.toLong(),
      homepage = homepage,
      imdbId = imdbId,
      knownForDepartment = knownForDepartment,
      placeOfBirth = placeOfBirth,
      profilePath = profilePath,
      insertedAt = insertedAt,
      id = id,
    )

  override fun deleteFromPerson(
    id: Long,
    field: PersonChangeField,
  ) = database.personDetailsEntityQueries.deleteFromPerson(
    biography = field == PersonChangeField.BIOGRAPHY,
    name = field == PersonChangeField.NAME,
    birthday = field == PersonChangeField.BIRTHDAY,
    deathday = field == PersonChangeField.DEATHDAY,
    gender = field == PersonChangeField.GENDER,
    homepage = field == PersonChangeField.HOMEPAGE,
    imdbId = field == PersonChangeField.IMDB_ID,
    knownForDepartment = field == PersonChangeField.KNOWN_FOR_DEPARTMENT,
    placeOfBirth = field == PersonChangeField.PLACE_OF_BIRTH,
    profilePath = field == PersonChangeField.PROFILE_PATH,
    id = id,
  )

  override fun insertPersonCredits(id: Long) {
    val currentEpochSeconds = clock.currentEpochSeconds()

    database
      .personCreditsEntityQueries
      .insertPersonCredits(
        PersonCreditsEntity = PersonCreditsEntity(
          id = id,
          insertedAt = currentEpochSeconds,
        ),
      )
  }

  override fun insertPersonCastCredits(cast: List<PersonCastCreditEntity>) = database.transaction {
    cast.forEach {
      database.personCastCreditEntityQueries.insertPersonCastCredit(it)
    }
  }

  override fun insertPersonCrewCredits(crew: List<PersonCrewCreditEntity>) = database.transaction {
    crew.forEach {
      database.personCrewCreditEntityQueries.insertPersonCrewCredit(it)
    }
  }

  override fun fetchPersonCombinedCredits(id: Long): Flow<PersonCombinedCreditsEntity?> {
    val personCreditId = fetchPersonCredits(id)
    val castCredits = fetchPersonCastCredits(id)
    val crewCredits = fetchPersonCrewCredits(id)

    return combine(personCreditId, castCredits, crewCredits) { personCredit, cast, crew ->
      if (personCredit != null) {
        PersonCombinedCreditsEntity(
          id = id,
          cast = cast,
          crew = crew,
        )
      } else {
        null
      }
    }
  }

  private fun fetchPersonCredits(id: Long): Flow<PersonCreditsEntity?> = database
    .personCreditsEntityQueries
    .fetchPersonCredits(id)
    .asFlow()
    .mapToOneOrNull(context = dispatcher.io)

  private fun fetchPersonCastCredits(id: Long): Flow<List<CastCreditsWithMedia>> = database
    .personCastCreditEntityQueries
    .castCreditsWithMedia(id)
    .asFlow()
    .mapToList(context = dispatcher.io)

  private fun fetchPersonCrewCredits(id: Long): Flow<List<CrewCreditsWithMedia>> = database
    .personCrewCreditEntityQueries
    .crewCreditsWithMedia(id)
    .asFlow()
    .mapToList(context = dispatcher.io)

  override fun insertGuestStars(
    showId: Int,
    season: Int,
    episode: Int,
    guestStars: List<Person>,
  ) {
    database.transaction {
      val insertedPersonIds = mutableSetOf<Long>()
      val insertedCreditIds = mutableSetOf<String>()

      guestStars.forEach { person ->
        if (person.id !in insertedPersonIds) {
          database.personEntityQueries.insertPerson(
            PersonEntity(
              id = person.id,
              name = person.name,
              originalName = person.name,
              profilePath = person.profilePath,
              knownForDepartment = person.knownForDepartment,
              gender = person.gender.value.toLong(),
            ),
          )
          insertedPersonIds += person.id
        }

        person.role
          .filterIsInstance<PersonRole.SeriesActor>()
          .forEach { role ->
            if (role.creditId !in insertedCreditIds) {
              database.personRoleEntityQueries.insertRole(
                PersonRoleEntity(
                  creditId = role.creditId,
                  character = role.character,
                  castId = person.id,
                ),
              )

              database.seasonGuestStarRoleEntityQueries.insertSeasonGuestStarRole(
                showId = showId.toLong(),
                season = season.toLong(),
                creditId = role.creditId,
                episode = episode.toLong(),
                episodeCount = role.totalEpisodes?.toLong(),
                displayOrder = role.order?.toLong() ?: -1,
              )
              insertedCreditIds += role.creditId
            }
          }
      }
    }
  }

  override fun fetchGuestStars(
    showId: Int,
    season: Int,
  ): Flow<List<Person>> = database
    .transactionWithResult {
      database
        .seasonGuestStarRoleEntityQueries
        .fetchSeasonGuestStars(
          season = season.toLong(),
          showId = showId.toLong(),
        )
        .asFlow()
        .mapToList(dispatcher.io)
        .map { entities ->
          entities
            .groupBy { it.id }
            .map { (personId, roles) ->
              val firstRole = roles.first()
              Person(
                id = personId,
                name = firstRole.name,
                profilePath = firstRole.profilePath,
                gender = Gender.from(firstRole.gender.toInt()),
                knownForDepartment = firstRole.knownForDepartment,
                role = listOf(
                  PersonRole.SeriesActor(
                    character = firstRole.character,
                    creditId = firstRole.creditId,
                    totalEpisodes = firstRole.episodeCount?.toInt(),
                    order = firstRole.displayOrder.toInt(),
                  ),
                ),
              )
            }
        }
    }
}
