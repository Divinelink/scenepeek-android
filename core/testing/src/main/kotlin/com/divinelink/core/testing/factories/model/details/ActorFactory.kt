package com.divinelink.core.testing.factories.model.details

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person

object ActorFactory {

  fun JackNicholson() = Person(
    id = 10,
    name = "Jack Nicholson",
    profilePath = "jack_nicholson.jpg",
    knownForDepartment = "Acting",
    role = listOf(
      PersonRole.MovieActor(
        character = "Here's Johnny!",
        order = 0,
      ),
    ),
  )

  fun AaronPaul() = Person(
    id = 20,
    name = "Aaron Paul",
    profilePath = "Aaron_paul.jpg",
    knownForDepartment = "Acting",
    role = listOf(
      PersonRole.MovieActor(
        character = "Jessee Pinkman",
        order = 1,
      ),
    ),
  )

  fun all() = listOf(
    JackNicholson(),
    AaronPaul(),
  )
}
