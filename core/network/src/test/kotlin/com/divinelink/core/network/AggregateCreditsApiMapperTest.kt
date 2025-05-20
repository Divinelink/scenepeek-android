package com.divinelink.core.network

import JvmUnitTestDemoAssetManager
import com.divinelink.core.data.details.mapper.api.map
import com.divinelink.core.fixtures.details.credits.SeriesCrewListFactory
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test

class AggregateCreditsApiMapperTest {

  @Test
  fun `test aggregated credits mapper`() = runTest {
    val json = JvmUnitTestDemoAssetManager.open("credits.json")

    json.use { inputStream ->
      val credits = inputStream.readBytes().decodeToString().trimIndent()

      val serializer = AggregateCreditsApi.serializer()
      val aggregateCreditsApi = Json.decodeFromString(serializer, credits)
      val aggregatedCredits = aggregateCreditsApi.map()

      assertThat(aggregatedCredits.id).isEqualTo(2316)
      assertThat(aggregatedCredits.cast.size).isEqualTo(692)
      assertThat(aggregatedCredits.crewDepartments.size).isEqualTo(10)

      val cast = aggregatedCredits.cast.take(8)

      assertThat(cast).isEqualTo(AggregatedCreditsFactory.credits().cast)

      val artDepartment = aggregatedCredits.crewDepartments[0]
      assertThat(artDepartment).isEqualTo(SeriesCrewListFactory.art())

      val cameraDepartment = aggregatedCredits.crewDepartments[1]
      assertThat(cameraDepartment).isEqualTo(SeriesCrewListFactory.camera())

      val costumeAndMakeUpDepartment = aggregatedCredits.crewDepartments[2]
      assertThat(costumeAndMakeUpDepartment).isEqualTo(SeriesCrewListFactory.costumeAndMakeUp())
    }
  }
}
