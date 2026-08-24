package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MediaListRequestTest {

  @Test
  fun `test toRequest for favorites section`() {
    MediaListSection.Saved.toRequest(MediaType.TV, "") shouldBe null
    MediaListSection.Saved.toRequest(MediaType.MOVIE, "") shouldBe null
    MediaListSection.Saved.toRequest(MediaType.PERSON, "") shouldBe null
    MediaListSection.Saved.toRequest(MediaType.UNKNOWN, "") shouldBe null
  }

  @Test
  fun `test toRequest for top rated section`() {
    MediaListSection.TopRated.toRequest(
      mediaType = MediaType.TV,
      minDate = "",
    ) shouldBe MediaListRequest.TopRated(
      MediaType.TV,
    )
    MediaListSection.TopRated.toRequest(
      mediaType = MediaType.MOVIE,
      minDate = "",
    ) shouldBe MediaListRequest.TopRated(
      MediaType.MOVIE,
    )
    MediaListSection.TopRated.toRequest(
      mediaType = MediaType.PERSON,
      minDate = "",
    ) shouldBe MediaListRequest.TopRated(
      MediaType.PERSON,
    )
    MediaListSection.TopRated.toRequest(
      mediaType = MediaType.UNKNOWN,
      minDate = "",
    ) shouldBe MediaListRequest.TopRated(
      MediaType.UNKNOWN,
    )
  }

  @Test
  fun `test toRequest for popular section`() {
    MediaListSection.Popular(MediaType.TV).toRequest(
      mediaType = MediaType.UNKNOWN,
      minDate = "",
    ) shouldBe MediaListRequest.Popular(MediaType.TV)

    MediaListSection.Popular(MediaType.MOVIE).toRequest(
      mediaType = MediaType.UNKNOWN,
      minDate = "",
    ) shouldBe MediaListRequest.Popular(MediaType.MOVIE)

    MediaListSection.Popular(MediaType.PERSON).toRequest(
      mediaType = MediaType.UNKNOWN,
      minDate = "",
    ) shouldBe MediaListRequest.Popular(MediaType.PERSON)

    MediaListSection.Popular(MediaType.UNKNOWN).toRequest(
      mediaType = MediaType.TV,
      minDate = "",
    ) shouldBe MediaListRequest.Popular(MediaType.UNKNOWN)
  }

  @Test
  fun `test toRequest for upcoming section`() {
    fun createUpcomingSection(mediaType: MediaType) = MediaListSection.Upcoming(
      mediaType = mediaType,
    ).toRequest(mediaType, minDate = "2024-02-02")

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
