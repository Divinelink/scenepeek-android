package com.divinelink.core.network.media.mapper.find

import JvmUnitTestDemoAssetManager
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.media.model.find.FindByIdResponseApi
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class FindMyIdResponseApiMapperTest {

  @Test
  fun `test FindByIdResponseApiMapper for movie`() = JvmUnitTestDemoAssetManager
    .open("find-by-id-movie.json")
    .use {
      val findById = it.readBytes().decodeToString().trimIndent()

      val serializer = FindByIdResponseApi.serializer()
      val findByIdApi = localJson.decodeFromString(serializer, findById)

      val mappedMedia = findByIdApi.map()

      assertThat(mappedMedia).isEqualTo(
        MediaItem.Media.Movie(
          id = 550,
          name = "Fight Club",
          posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
          releaseDate = "1999-10-15",
          voteAverage = 8.438,
          voteCount = 29700,
          overview = "A ticking-time-bomb insomniac and a slippery soap salesman.",
          isFavorite = false,
          backdropPath = "/hZkgoQYus5vegHoetLkCJzb17zJ.jpg",
          accountRating = null,
        ),
      )
    }

  @Test
  fun `test FindByIdResponseApiMapper for tv`() = JvmUnitTestDemoAssetManager
    .open("find-by-id-tv.json")
    .use {
      val findById = it.readBytes().decodeToString().trimIndent()

      val serializer = FindByIdResponseApi.serializer()
      val findByIdApi = localJson.decodeFromString(serializer, findById)

      val mappedMedia = findByIdApi.map()

      assertThat(mappedMedia).isEqualTo(
        MediaItem.Media.TV(
          id = 95396,
          name = "Severance",
          posterPath = "/pPHpeI2X1qEd1CS1SeyrdhZ4qnT.jpg",
          releaseDate = "2022-02-17",
          voteAverage = 8.4,
          voteCount = 1433,
          overview = "Mark leads a team of office workers whose memories have been lost.",
          isFavorite = false,
          accountRating = null,
          backdropPath = "/5OsiT39OiZNdD0v2LiAcI2TpSYj.jpg",
        ),
      )
    }

  @Test
  fun `test FindByIdResponseApiMapper for person`() = JvmUnitTestDemoAssetManager
    .open("find-by-id-person.json")
    .use {
      val findById = it.readBytes().decodeToString().trimIndent()

      val serializer = FindByIdResponseApi.serializer()
      val findByIdApi = localJson.decodeFromString(serializer, findById)

      val mappedMedia = findByIdApi.map()

      assertThat(mappedMedia).isEqualTo(
        MediaItem.Person(
          id = 36801,
          name = "Adam Scott",
          profilePath = "/b82C29R6fGiPoqIglQ4lzS6q2YX.jpg",
          gender = Gender.MALE,
          knownForDepartment = "Acting",
        ),
      )
    }

  @Test
  fun `test FindByIdResponseApiMapper for tv episode`() = JvmUnitTestDemoAssetManager
    .open("find-by-id-tv-episode.json")
    .use {
      val findById = it.readBytes().decodeToString().trimIndent()

      val serializer = FindByIdResponseApi.serializer()
      val findByIdApi = localJson.decodeFromString(serializer, findById)

      val mappedMedia = findByIdApi.map()

      assertThat(mappedMedia).isEqualTo(MediaItem.Unknown)
    }
}
