package com.divinelink.core.fixtures.model.details

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender

object ActorFactory {

  fun JackNicholson() = MediaItem.Person(
    id = 10,
    name = "Jack Nicholson",
    profilePath = "jack_nicholson.jpg",
    knownForDepartment = "Acting",
    gender = Gender.NOT_SET,
    role = listOf(
      PersonRole.MovieActor(
        character = "Here's Johnny!",
        order = 0,
      ),
    ),
  )

  fun AaronPaul() = MediaItem.Person(
    id = 20,
    name = "Aaron Paul",
    profilePath = "Aaron_paul.jpg",
    knownForDepartment = "Acting",
    gender = Gender.NOT_SET,
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
