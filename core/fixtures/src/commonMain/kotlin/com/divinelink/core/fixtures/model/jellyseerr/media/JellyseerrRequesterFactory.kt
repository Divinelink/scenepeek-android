package com.divinelink.core.fixtures.model.jellyseerr.media

import com.divinelink.core.model.jellyseerr.media.JellyseerrRequester

object JellyseerrRequesterFactory {

  val scenepeek = JellyseerrRequester(
    id = 6,
    displayName = "ScenePeek",
  )

  fun bob() = JellyseerrRequester(
    id = 1,
    displayName = "Bob Odenkirk",
  )

  fun rhea() = JellyseerrRequester(
    id = 2,
    displayName = "Rhea Seehorn",
  )
}
