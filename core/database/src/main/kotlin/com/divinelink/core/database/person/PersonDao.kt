package com.divinelink.core.database.person

interface PersonDao {

  fun fetchPersonById(id: Long): PersonEntity?

  fun insertPerson(person: PersonEntity)
}
