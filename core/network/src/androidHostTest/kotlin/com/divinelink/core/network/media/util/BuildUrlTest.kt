package com.divinelink.core.network.media.util

import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.media.MediaType
import com.google.common.truth.Truth.assertThat
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class BuildUrlTest {

  @Test
  fun `test buildFetchDetailsUrl for TV`() {
    val url = buildFetchDetailsUrl(
      id = 1234,
      media = MediaType.TV,
      appendToResponse = true,
    )

    assertThat(url).isEqualTo(
      "https://api.themoviedb.org/3/tv/1234?language=en-US&append_to_response=external_ids",
    )
  }

  @Test
  fun `test buildFetchDetailsUrl for TV with append to response false`() {
    val url = buildFetchDetailsUrl(
      id = 1234,
      media = MediaType.TV,
      appendToResponse = false,
    )

    assertThat(url).isEqualTo(
      "https://api.themoviedb.org/3/tv/1234?language=en-US",
    )
  }

  @Test
  fun `test buildFetchDetailsUrl for MOVIE`() {
    val url = buildFetchDetailsUrl(
      id = 1234,
      media = MediaType.MOVIE,
      appendToResponse = true,
    )

    assertThat(url).isEqualTo(
      "https://api.themoviedb.org/3/movie/1234?language=en-US&append_to_response=credits",
    )
  }

  @Test
  fun `test buildFetchDetailsUrl for MOVIE with append to response false`() {
    val url = buildFetchDetailsUrl(
      id = 1234,
      media = MediaType.MOVIE,
      appendToResponse = false,
    )

    assertThat(url).isEqualTo(
      "https://api.themoviedb.org/3/movie/1234?language=en-US",
    )
  }

  @Test
  fun `test buildFindByIdUrl`() {
    val url = buildFindByIdUrl(
      externalId = "tt1234",
    )

    assertThat(url).isEqualTo(
      "https://api.themoviedb.org/3/find/tt1234?external_source=imdb_id",
    )
  }

  @Test
  fun `test buildMovieGenreUrl`() {
    buildGenreUrl(
      MediaType.MOVIE,
    ) shouldBe "https://api.themoviedb.org/3/genre/movie/list?language=en"
  }

  @Test
  fun `test buildTvGenreUrl`() {
    buildGenreUrl(
      MediaType.TV,
    ) shouldBe "https://api.themoviedb.org/3/genre/tv/list?language=en"
  }

  @Test
  fun `test buildFetchMediaListUrl for trending`() {
    buildFetchMediaListUrl(
      section = HomeSection.TrendingAll,
      page = 1,
    ) shouldBe "https://api.themoviedb.org/3/trending/all/day?language=en-US&page=1"
  }

  @Test
  fun `test buildFetchMediaListUrl for popular movies`() {
    buildFetchMediaListUrl(
      section = HomeSection.Popular(MediaType.MOVIE),
      page = 1,
    ) shouldBe "https://api.themoviedb.org/3" +
      "/discover" +
      "/movie" +
      "?language=en-US" +
      "&page=1" +
      "&sort_by=popularity.desc" +
      "&vote_count.gte=50"
  }

  @Test
  fun `test buildFetchMediaListUrl for popular tv`() {
    buildFetchMediaListUrl(
      section = HomeSection.Popular(MediaType.TV),
      page = 1,
    ) shouldBe "https://api.themoviedb.org/3" +
      "/discover" +
      "/tv" +
      "?language=en-US" +
      "&page=1&sort_by=popularity.desc" +
      "&vote_count.gte=50"
  }

  @Test
  fun `test buildFetchMediaListUrl for upcoming movies`() {
    buildFetchMediaListUrl(
      section = HomeSection.Upcoming(
        mediaType = MediaType.MOVIE,
        minDate = "2021-12-01"
      ),
      page = 1,
    ) shouldBe "https://api.themoviedb.org/3" +
      "/discover" +
      "/movie" +
      "?language=en-US" +
      "&page=1" +
      "&primary_release_date.gte=2021-12-01"
  }

  @Test
  fun `test buildFetchMediaListUrl for upcoming tv`() {
    buildFetchMediaListUrl(
      section = HomeSection.Upcoming(
        mediaType = MediaType.TV,
        minDate = "2021-12-01"
      ),
      page = 1,
    ) shouldBe "https://api.themoviedb.org/3" +
      "/discover" +
      "/tv?language=en-US" +
      "&page=1" +
      "&first_air_date.gte=2021-12-01"
  }
}
