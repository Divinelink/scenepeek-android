package com.divinelink.core.network.list.mapper.details

import JvmUnitTestDemoAssetManager
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.media.model.search.multi.mapper.mapToMedia
import kotlin.test.Test
import kotlin.test.assertEquals

class ListDetailsResponseMapperTest {

  @Test
  fun `test ListDetailsResponse mapping`() = JvmUnitTestDemoAssetManager
    .open("list-details.json")
    .use {
      val responseJson = it.readBytes().decodeToString().trimIndent()
      val listDetailsResponse = localJson.decodeFromString(
        ListDetailsResponse.serializer(),
        responseJson,
      )

      val mapped = listDetailsResponse.map()

      assertEquals(mapped.name, "Elsolist")
      assertEquals(mapped.media.size, 20)
      assertEquals(
        mapped.media[0],
        MediaItem.Media.Movie(
          id = 1065504,
          name = "Elsa",
          posterPath = "/dg5tXrH3ukq6at0N7XLfudJk6CJ.jpg",
          releaseDate = "2023-01-20",
          voteAverage = 0.0,
          voteCount = 0,
          overview = "A Deafblind fencer and author competes in all arenas " +
            "just for the right to be seen.",
          isFavorite = false,
        ),
      )
      assertEquals(
        mapped.media[3],
        MediaItem.Media.TV(
          id = 90802,
          name = "The Sandman",
          posterPath = "/dhbqyYjLfTNAHodDDjDELzwO6F4.jpg",
          releaseDate = "2022-08-05",
          voteAverage = 7.9,
          voteCount = 2230,
          overview = "After years of imprisonment, Morpheus — the King of Dreams — " +
            "embarks on a journey across worlds to find what was stolen from him and" +
            " restore his power.",
          isFavorite = false,
        ),
      )
      assertEquals(
        mapped,
        ListDetails(
          name = "Elsolist",
          media = listDetailsResponse.results.mapToMedia(),
        ),
      )
    }
}
