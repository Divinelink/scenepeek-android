package com.divinelink.core.database.person

import com.divinelink.core.database.person.credits.CastCreditsWithMedia
import com.divinelink.core.database.person.credits.CrewCreditsWithMedia

data class PersonCombinedCreditsEntity(
  val id: Long,
  val cast: List<CastCreditsWithMedia>,
  val crew: List<CrewCreditsWithMedia>,
)
