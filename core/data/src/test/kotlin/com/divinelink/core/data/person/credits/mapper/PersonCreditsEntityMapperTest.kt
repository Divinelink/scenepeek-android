package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.core.testing.factories.entity.person.credits.CastCreditsWithMediaFactory
import com.divinelink.core.testing.factories.entity.person.credits.CrewCreditsWithMediaFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class PersonCreditsEntityMapperTest {

  @Test
  fun `test cast credit for movie entity mapper`() {
    val entity = CastCreditsWithMediaFactory.bruceAlmighty
    val mapped = entity.map(
      isTvFavorite = false,
      isMovieFavorite = false,
    )

    assertThat(mapped).isEqualTo(
      PersonCredit(
        creditId = "52fe4236c3a36847f800c65f",
        media = MediaItemFactory.bruceAlmighty(),
        role = PersonRole.MovieActor(
          character = "Evan Baxter",
          order = 6,
        ),
      ),
    )
  }

  @Test
  fun `test cast credit for movie entity mapper with favorite`() {
    val entity = CastCreditsWithMediaFactory.bruceAlmighty
    val mapped = entity.map(
      isTvFavorite = false,
      isMovieFavorite = true,
    )

    assertThat(mapped).isEqualTo(
      PersonCredit(
        creditId = "52fe4236c3a36847f800c65f",
        media = MediaItemFactory.bruceAlmighty().copy(isFavorite = true),
        role = PersonRole.MovieActor(
          character = "Evan Baxter",
          order = 6,
        ),
      ),
    )
  }

  @Test
  fun `test cast credit for tv entity mapper`() {
    fun entity() = CastCreditsWithMediaFactory.theOffice

    assertThat(
      entity().map(
        isTvFavorite = false,
        isMovieFavorite = false,
      ),
    ).isEqualTo(
      PersonCredit(
        creditId = "525730a9760ee3776a344705",
        media = MediaItemFactory.theOffice(),
        role = PersonRole.SeriesActor(
          character = "Michael Scott",
          creditId = "525730a9760ee3776a344705",
          totalEpisodes = 140,
        ),
      ),
    )
  }

  @Test
  fun `test cast credit for tv entity mapper with favorite`() {
    fun entity() = CastCreditsWithMediaFactory.theOffice

    assertThat(
      entity().map(
        isTvFavorite = true,
        isMovieFavorite = false,
      ),
    ).isEqualTo(
      PersonCredit(
        creditId = "525730a9760ee3776a344705",
        media = MediaItemFactory.theOffice().copy(isFavorite = true),
        role = PersonRole.SeriesActor(
          character = "Michael Scott",
          creditId = "525730a9760ee3776a344705",
          totalEpisodes = 140,
        ),
      ),
    )
  }

  @Test
  fun `test crew credits with media mapper`() {
    val crewEntity = CrewCreditsWithMediaFactory.the40YearOldVirgin

    assertThat(
      crewEntity.map(
        isTvFavorite = false,
        isMovieFavorite = false,
      ),
    ).isEqualTo(
      PersonCredit(
        creditId = "52fe446ac3a36847f8094c49",
        media = MediaItemFactory.the40YearOldVirgin(),
        role = PersonRole.Crew(
          job = "Screenplay",
          creditId = "52fe446ac3a36847f8094c49",
          department = "Writing",
        ),
      ),
    )
  }

  @Test
  fun `test crew credits with media mapper with favorite`() {
    val crewEntity = CrewCreditsWithMediaFactory.the40YearOldVirgin

    assertThat(
      crewEntity.map(
        isTvFavorite = false,
        isMovieFavorite = true,
      ),
    ).isEqualTo(
      PersonCredit(
        creditId = "52fe446ac3a36847f8094c49",
        media = MediaItemFactory.the40YearOldVirgin().copy(isFavorite = true),
        role = PersonRole.Crew(
          job = "Screenplay",
          creditId = "52fe446ac3a36847f8094c49",
          department = "Writing",
        ),
      ),
    )
  }

  @Test
  fun `test crew credits with media for tv mapper`() {
    val entity = CrewCreditsWithMediaFactory.riot

    assertThat(
      entity.map(
        isTvFavorite = false,
        isMovieFavorite = false,
      ),
    ).isEqualTo(
      PersonCredit(
        creditId = "53762236c3a3681ed4001579",
        media = MediaItemFactory.riot(),
        role = PersonRole.Crew(
          job = "Executive Producer",
          department = "Production",
          totalEpisodes = 4,
          creditId = "53762236c3a3681ed4001579",
        ),
      ),
    )
  }

  @Test
  fun `test crew credits with media for tv mapper with favorite`() {
    val entity = CrewCreditsWithMediaFactory.riot

    assertThat(
      entity.map(
        isTvFavorite = true,
        isMovieFavorite = false,
      ),
    ).isEqualTo(
      PersonCredit(
        creditId = "53762236c3a3681ed4001579",
        media = MediaItemFactory.riot().copy(isFavorite = true),
        role = PersonRole.Crew(
          job = "Executive Producer",
          department = "Production",
          totalEpisodes = 4,
          creditId = "53762236c3a3681ed4001579",
        ),
      ),
    )
  }
}
