package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MediaListRequestTest {

  @Test
  fun `test toRequest for favorites section`() {
    MediaListSection.Favorites.toRequest(MediaType.TV) shouldBe null
    MediaListSection.Favorites.toRequest(MediaType.MOVIE) shouldBe null
    MediaListSection.Favorites.toRequest(MediaType.PERSON) shouldBe null
    MediaListSection.Favorites.toRequest(MediaType.UNKNOWN) shouldBe null
  }

  @Test
  fun `test toRequest for top rated section`() {
    MediaListSection.TopRated.toRequest(MediaType.TV) shouldBe MediaListRequest.TopRated(
      MediaType.TV,
    )
    MediaListSection.TopRated.toRequest(MediaType.MOVIE) shouldBe MediaListRequest.TopRated(
      MediaType.MOVIE,
    )
    MediaListSection.TopRated.toRequest(MediaType.PERSON) shouldBe MediaListRequest.TopRated(
      MediaType.PERSON,
    )
    MediaListSection.TopRated.toRequest(MediaType.UNKNOWN) shouldBe MediaListRequest.TopRated(
      MediaType.UNKNOWN,
    )
  }

  @Test
  fun `test toRequest for popular section`() {
    MediaListSection.Popular(MediaType.TV).toRequest(
      MediaType.UNKNOWN,
    ) shouldBe MediaListRequest.Popular(MediaType.TV)

    MediaListSection.Popular(MediaType.MOVIE).toRequest(
      MediaType.UNKNOWN,
    ) shouldBe MediaListRequest.Popular(MediaType.MOVIE)

    MediaListSection.Popular(MediaType.PERSON).toRequest(
      MediaType.UNKNOWN,
    ) shouldBe MediaListRequest.Popular(MediaType.PERSON)

    MediaListSection.Popular(MediaType.UNKNOWN).toRequest(
      MediaType.TV,
    ) shouldBe MediaListRequest.Popular(MediaType.UNKNOWN)
  }

  @Test
  fun `test toRequest for upcoming section`() {
    fun createUpcomingSection(mediaType: MediaType) = MediaListSection.Upcoming(
      minDate = "2024-02-02",
      mediaType = mediaType,
    ).toRequest(mediaType)

    createUpcomingSection(MediaType.TV) shouldBe MediaListRequest.Upcoming(
      minDate = "2024-02-02",
      mediaType = MediaType.TV,
    )
    createUpcomingSection(MediaType.MOVIE) shouldBe MediaListRequest.Upcoming(
      minDate = "2024-02-02",
      mediaType = MediaType.MOVIE,
    )
    createUpcomingSection(MediaType.PERSON) shouldBe MediaListRequest.Upcoming(
      minDate = "2024-02-02",
      mediaType = MediaType.PERSON,
    )
    createUpcomingSection(MediaType.UNKNOWN) shouldBe MediaListRequest.Upcoming(
      minDate = "2024-02-02",
      mediaType = MediaType.UNKNOWN,
    )
  }
}
