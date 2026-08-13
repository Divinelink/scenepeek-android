package com.divinelink.core.fixtures.data.opendata

import com.divinelink.core.data.opendata.OpenDataRepository
import com.divinelink.core.model.locale.LocaleCode

class TestOpenDataRepository : OpenDataRepository {
  private var wikiTitles: Result<Map<LocaleCode, String>> = Result.success(emptyMap())

  override suspend fun getWikiTitles(
    wikidataId: String,
  ): Result<Map<LocaleCode, String>> = wikiTitles

  /**
   * Mock functions
   */
  fun mockWikiTitles(
    result: Result<Map<LocaleCode, String>>,
  ) {
    wikiTitles = result
  }
}
