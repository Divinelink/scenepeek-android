package com.divinelink.core.fixtures.core.network.jellyseerr.model

import com.divinelink.core.network.jellyseerr.model.RequestedByResponse

object RequestedByResponseFactory {

  fun bob() = RequestedByResponse(
    id = 1,
    displayName = "Bob Odenkirk",
  )

  fun rhea() = RequestedByResponse(
    id = 2,
    displayName = "Rhea Seehorn",
  )
}
