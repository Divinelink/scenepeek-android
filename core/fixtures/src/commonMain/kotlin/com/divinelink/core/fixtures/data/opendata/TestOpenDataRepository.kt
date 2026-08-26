package com.divinelink.core.fixtures.data.opendata

import com.divinelink.core.data.opendata.OpenDataRepository
import com.divinelink.core.fixtures.model.FooException
import com.divinelink.core.model.locale.LocaleCode
import com.divinelink.core.model.media.MediaReference

class TestOpenDataRepository : OpenDataRepository {
  private var wikiTitles: Result<Map<LocaleCode, String>> = Result.failure(FooException())
  private var media: Result<MediaReference> = Result.failure(FooException())

  override suspend fun getWikiTitles(wikidataId: String): Result<Map<LocaleCode, String>> =
    wikiTitles

  override suspend fun getMediaFromLetterboxd(url: String): Result<MediaReference> = media

  /**
   * Mock functions
   */
  fun mockWikiTitles(result: Result<Map<LocaleCode, String>>) {
    wikiTitles = result
  }

  fun mockGetMediaFromLetterboxd(result: Result<MediaReference>) {
    media = result
  }
}
