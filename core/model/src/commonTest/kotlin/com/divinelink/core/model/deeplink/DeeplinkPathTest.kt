package com.divinelink.core.model.deeplink

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DeeplinkPathTest {

  @Test
  fun `should return null for null input`() {
    val result = null.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should return null for empty string input`() {
    val result = "".extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should return null for whitespace-only input`() {
    val result = "   ".extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should extract movie ID from simple movie URL`() {
    val url = "https://scenepeek.app/movie/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should extract movie ID from ScenePeek protocol URL`() {
    val url = "scenepeek://movie/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should extract movie ID from MovieDB URL`() {
    val url = "https://themoviedb.org/movie/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should extract movie ID with complex title containing special characters`() {
    val url = "https://scenepeek.app/movie/123-title-with-dashes-and_underscores-and!@#"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should extract movie ID with international characters in title`() {
    val url = "https://scenepeek.app/movie/123-电影标题"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should return null for movie URL with no ID`() {
    val url = "https://scenepeek.app/movie/"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should return null for movie URL with invalid ID`() {
    val url = "https://scenepeek.app/movie/abc-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should extract TV show ID from simple TV URL`() {
    val url = "https://scenepeek.app/tv/693134-dune-part-two"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.TV(693134)
  }

  @Test
  fun `should extract TV show ID from ScenePeek protocol URL`() {
    val url = "scenepeek://tv/693134-dune-part-two"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.TV(693134)
  }

  @Test
  fun `should extract TV show ID from MovieDB URL`() {
    val url = "https://themoviedb.org/tv/693134-dune-part-two"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.TV(693134)
  }

  @Test
  fun `should extract TV show ID with complex title`() {
    val url = "https://scenepeek.app/tv/693134-dune-part-two-with-special-chars!@#"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.TV(693134)
  }

  @Test
  fun `should return null for TV URL with no ID`() {
    val url = "https://scenepeek.app/tv/"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should return null for TV URL with invalid ID`() {
    val url = "https://scenepeek.app/tv/abc-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should extract collection ID from collection URL`() {
    val url = "https://scenepeek.app/collection/456-best-movies"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Collection(456)
  }

  @Test
  fun `should extract collection ID from ScenePeek protocol URL`() {
    val url = "scenepeek://collection/456-best-movies"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Collection(456)
  }

  @Test
  fun `should extract collection ID from MovieDB URL`() {
    val url = "https://themoviedb.org/collection/456-best-movies"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Collection(456)
  }

  @Test
  fun `should return null for collection URL with no ID`() {
    val url = "https://scenepeek.app/collection/"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should return null for collection URL with invalid ID`() {
    val url = "https://scenepeek.app/collection/abc-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should extract list ID from list URL`() {
    val url = "https://scenepeek.app/list/789-my-list"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.List(789)
  }

  @Test
  fun `should extract list ID from ScenePeek protocol URL`() {
    val url = "scenepeek://list/789-my-list"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.List(789)
  }

  @Test
  fun `should extract list ID from MovieDB URL`() {
    val url = "https://themoviedb.org/list/789-my-list"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.List(789)
  }

  @Test
  fun `should return null for list URL with no ID`() {
    val url = "https://scenepeek.app/list/"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should return null for list URL with invalid ID`() {
    val url = "https://scenepeek.app/list/abc-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should extract person ID from person URL`() {
    val url = "https://scenepeek.app/person/987-director-name"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Person(987)
  }

  @Test
  fun `should extract person ID from ScenePeek protocol URL`() {
    val url = "scenepeek://person/987-director-name"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Person(987)
  }

  @Test
  fun `should extract person ID from MovieDB URL`() {
    val url = "https://themoviedb.org/person/987-director-name"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Person(987)
  }

  @Test
  fun `should return null for person URL with no ID`() {
    val url = "https://scenepeek.app/person/"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should return null for person URL with invalid ID`() {
    val url = "https://scenepeek.app/person/abc-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should handle https prefix with www`() {
    val url = "https://www.scenepeek.app/movie/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle http prefix with www`() {
    val url = "http://www.scenepeek.app/movie/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle scenepeek prefix with www`() {
    val url = "scenepeek://www.movie/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle case insensitive media type matching`() {
    val url = "https://scenepeek.app/MOVIE/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle case insensitive media type matching with lowercase`() {
    val url = "https://scenepeek.app/movIe/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle URLs with query parameters`() {
    val url = "https://scenepeek.app/movie/123-title?param=value"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle URLs with fragments`() {
    val url = "https://scenepeek.app/movie/123-title#section"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle URLs with both query and fragment`() {
    val url = "https://scenepeek.app/movie/123-title?param=value#section"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle URLs with trailing slash`() {
    val url = "https://scenepeek.app/movie/123-title/"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle complex nested URL with multiple segments`() {
    val url = "https://scenepeek.app/tv/693134-dune-part-two/episodes"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.TV(693134)
  }

  @Test
  fun `should handle edge case with only domain and no path`() {
    val url = "https://scenepeek.app"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should handle edge case with domain and slash only`() {
    val url = "https://scenepeek.app/"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should handle URLs that don't match known domains`() {
    val url = "https://other-site.com/movie/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should handle URLs with subdomains not in domain list`() {
    val url = "https://sub.scenepeek.app/movie/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should handle URLs with unknown media types`() {
    val url = "https://scenepeek.app/unknown/123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should handle URLs with negative IDs`() {
    val url = "https://scenepeek.app/movie/-123-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should handle URLs with very large IDs`() {
    val url = "https://scenepeek.app/movie/999999999999999999-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(id = 999999999999999999)
  }

  @Test
  fun `should handle URLs with zero ID`() {
    val url = "https://scenepeek.app/movie/0-title"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(0)
  }

  @Test
  fun `should work properly after normalizeUrl processing`() {
    val url = "https://SCENEPEEK.APP/MOVIE/123-TITLE"
    val result = url.extractRouteFromDeeplink()
    result shouldBe DeeplinkPath.Movie(123)
  }

  @Test
  fun `should handle URLs that normalize to empty string`() {
    val url = "https://scenepeek.app"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }

  @Test
  fun `should handle URLs that normalize to just slash`() {
    val url = "https://scenepeek.app/"
    val result = url.extractRouteFromDeeplink()
    result shouldBe null
  }
}
