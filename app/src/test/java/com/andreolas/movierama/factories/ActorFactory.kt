package com.andreolas.movierama.factories

import com.andreolas.movierama.details.domain.model.Actor

object ActorFactory {

  fun JackNicholson() = Actor(
    id = 10,
    name = "Jack Nicholson",
    profilePath = "jack_nicholson.jpg",
    character = "Here's Johnny!",
    order = 0
  )

  fun AaronPaul() = Actor(
    id = 20,
    name = "Aaron Paul",
    profilePath = "Aaron_paul.jpg",
    character = "Jessee Pinkman",
    order = 1
  )

  fun all() = listOf(
    JackNicholson(),
    AaronPaul()
  )
}
