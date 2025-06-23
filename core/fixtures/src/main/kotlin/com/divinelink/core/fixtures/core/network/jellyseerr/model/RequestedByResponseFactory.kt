package com.divinelink.core.fixtures.core.network.jellyseerr.model

import com.divinelink.core.network.jellyseerr.model.RequestedByResponse

object RequestedByResponseFactory {

  fun bob() = RequestedByResponse(
    displayName = "Bob Odenkirk",
    id = 1,
  )

  fun rhea() = RequestedByResponse(
    displayName = "Rhea Seehorn",
    id = 2,
  )
}
