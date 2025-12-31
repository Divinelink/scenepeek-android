package com.divinelink.core.model.details

import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class SeasonExtensionTest {

  @Test
  fun `test isAvailable with status is Media Available`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.AVAILABLE,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Media Partially Available`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Media Processing`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.PROCESSING,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Media Pending`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.PENDING,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Request Pending`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Request.PENDING,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Request Approved`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Request.APPROVED,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Request Failed`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Request.FAILED,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Season Pending`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Season.PENDING,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Season Processed`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Season.PROCESSING,
    )

    assertThat(season.isAvailable()).isTrue()
  }

  @Test
  fun `test isAvailable with status is Media Unknown`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.UNKNOWN,
    )

    assertThat(season.isAvailable()).isFalse()
  }

  @Test
  fun `test isAvailable with status is Request Unknown`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Request.UNKNOWN,
    )

    assertThat(season.isAvailable()).isFalse()
  }

  @Test
  fun `test isAvailable with status is Season Unknown`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Season.UNKNOWN,
    )

    assertThat(season.isAvailable()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Media Available`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.AVAILABLE,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Media Partially Available`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Media Processing`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.PROCESSING,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Media Pending`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.PENDING,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Request Pending`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Request.PENDING,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Request Approved`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Request.APPROVED,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Request Failed`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Request.FAILED,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Season Pending`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Season.PENDING,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Season Processed`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Season.PROCESSING,
    )

    assertThat(season.canBeRequested()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Media Unknown`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.UNKNOWN,
    )

    assertThat(season.canBeRequested()).isTrue()
  }

  @Test
  fun `test canBeRequested with status is Request Unknown`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Request.UNKNOWN,
    )

    assertThat(season.canBeRequested()).isTrue()
  }

  @Test
  fun `test canBeRequested with status is Season Unknown`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Season.UNKNOWN,
    )

    assertThat(season.canBeRequested()).isTrue()
  }

  @Test
  fun `test canBeRequested with null status`() {
    val season = SeasonFactory.season1().copy(
      status = null,
    )

    assertThat(season.canBeRequested()).isTrue()
  }

  @Test
  fun `test isAvailable with null status`() {
    val season = SeasonFactory.season1().copy(
      status = null,
    )

    assertThat(season.isAvailable()).isFalse()
  }

  @Test
  fun `test isAvailable with status is Media Deleted`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.DELETED,
    )

    assertThat(season.isAvailable()).isFalse()
  }

  @Test
  fun `test canBeRequested with status is Media Deleted`() {
    val season = SeasonFactory.season1().copy(
      status = JellyseerrStatus.Media.DELETED,
    )

    assertThat(season.canBeRequested()).isTrue()
  }
}
