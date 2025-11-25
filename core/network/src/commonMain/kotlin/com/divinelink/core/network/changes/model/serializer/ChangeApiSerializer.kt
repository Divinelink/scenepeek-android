package com.divinelink.core.network.changes.model.serializer

import com.divinelink.core.model.change.AlternativeTitlesValue
import com.divinelink.core.model.change.BackdropValue
import com.divinelink.core.model.change.CastValue
import com.divinelink.core.model.change.ChangeType
import com.divinelink.core.model.change.ChangeValue
import com.divinelink.core.model.change.PlotKeywordsValue
import com.divinelink.core.model.change.PosterValue
import com.divinelink.core.model.change.ProfileValue
import com.divinelink.core.model.change.ReleaseDatesValue
import com.divinelink.core.model.change.StringValue
import com.divinelink.core.model.change.TaglineValue
import com.divinelink.core.model.change.TitleLogoValue
import com.divinelink.core.model.change.VideosValue
import com.divinelink.core.network.changes.model.api.ChangeApi
import com.divinelink.core.network.changes.model.api.ChangeApi.Companion.JsonKeys
import com.divinelink.core.network.changes.model.api.ChangeItemApi
import com.divinelink.core.network.client.decode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.divinelink.core.network.changes.model.api.ChangeItemApi.Companion.JsonKeys as ItemJsonKeys

object ChangeApiSerializer : KSerializer<ChangeApi> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
    serialName = ChangeApi::class.simpleName.toString(),
  )

  override fun deserialize(decoder: Decoder): ChangeApi {
    val jsonInput = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")
    val json = jsonInput.decodeJsonElement().jsonObject

    val key = json[JsonKeys.KEY]?.jsonPrimitive?.content ?: ""

    val items = json.extractChangeItems()

    return ChangeApi(
      key = key,
      items = items,
    )
  }

  private fun JsonObject.extractChangeItems(): List<ChangeItemApi> {
    val key = this[JsonKeys.KEY]?.jsonPrimitive?.content

    val changeKey = ChangeType.from(key)

    return when (changeKey) {
      ChangeType.TRANSLATIONS,
      ChangeType.BIOGRAPHY,
      ChangeType.NAME,
      ChangeType.TIKTOD_ID,
      ChangeType.YOUTUBE_ID,
      ChangeType.FACEBOOK_ID,
      ChangeType.IMDB_ID,
      ChangeType.INSTAGRAM_ID,
      ChangeType.TWITTER_ID,
      ChangeType.BIRTHDAY,
      ChangeType.DAY_OF_DEATH,
      ChangeType.PLACE_OF_BIRTH,
      ChangeType.GENDER,
      ChangeType.HOMEPAGE,
      ChangeType.PRIMARY,
      -> extractStringFromJson()
      ChangeType.IMAGES -> extractImagesFromJson()
      ChangeType.VIDEOS -> extractFromJson<VideosValue>()
      ChangeType.CAST -> extractFromJson<CastValue>()
      ChangeType.PLOT_KEYWORDS -> extractFromJson<PlotKeywordsValue>()
      ChangeType.RELEASE_DATES -> extractFromJson<ReleaseDatesValue>()
      ChangeType.ALTERNATIVE_TITLES -> extractFromJson<AlternativeTitlesValue>()
      ChangeType.TAGLINE -> extractFromJson<TaglineValue>()
      ChangeType.ALSO_KNOWN_AS -> emptyList()
      ChangeType.UNKNOWN -> {
        // TODO Send log for missing type
        emptyList()
      }
    }
  }

  private fun JsonElement.extractImageValuesFromJson(): ChangeValue? = this.let {
    val imageType = ImageType.fromKey(this.jsonObject.keys.firstOrNull()) ?: return null

    when (imageType) {
      ImageType.PROFILE -> decode<ProfileValue>()
      ImageType.BACKDROP -> decode<BackdropValue>()
      ImageType.POSTER -> decode<PosterValue>()
      ImageType.TITLE_LOGO -> decode<TitleLogoValue>()
    }
  }

  override fun serialize(
    encoder: Encoder,
    value: ChangeApi,
  ) {
    error("Serialization is not supported")
  }

  private inline fun <reified T : ChangeValue> JsonObject.extractFromJson(): List<ChangeItemApi> =
    this[JsonKeys.ITEMS]?.jsonArray?.map { jsonElement ->

      val jsonObject = jsonElement.jsonObject

      val value = jsonObject[ItemJsonKeys.VALUE]?.decode<T>()
      val originalValue = jsonObject[ItemJsonKeys.ORIGINAL_VALUE]?.decode<T>()

      ChangeItemApi(
        id = jsonObject.getValue(ItemJsonKeys.ID).jsonPrimitive.content,
        action = jsonObject.getValue(ItemJsonKeys.ACTION).jsonPrimitive.content,
        time = jsonObject.getValue(ItemJsonKeys.TIME).jsonPrimitive.content,
        iso6391 = jsonObject.getValue(ItemJsonKeys.ISO_639_1).jsonPrimitive.content,
        iso31661 = jsonObject.getValue(ItemJsonKeys.ISO_3166_1).jsonPrimitive.content,
        value = value,
        originalValue = originalValue,
      )
    } ?: emptyList()

  private fun JsonObject.extractStringFromJson(): List<ChangeItemApi> =
    this[JsonKeys.ITEMS]?.jsonArray?.map { jsonElement ->
      val jsonObject = jsonElement.jsonObject

      val value = jsonObject[ItemJsonKeys.VALUE]
      val originalValue = jsonObject[ItemJsonKeys.ORIGINAL_VALUE]

      ChangeItemApi(
        id = jsonObject.getValue(ItemJsonKeys.ID).jsonPrimitive.content,
        action = jsonObject.getValue(ItemJsonKeys.ACTION).jsonPrimitive.content,
        time = jsonObject.getValue(ItemJsonKeys.TIME).jsonPrimitive.content,
        iso6391 = jsonObject.getValue(ItemJsonKeys.ISO_639_1).jsonPrimitive.content,
        iso31661 = jsonObject.getValue(ItemJsonKeys.ISO_3166_1).jsonPrimitive.content,
        value = value?.let { StringValue(it.jsonPrimitive.content) },
        originalValue = originalValue?.let { StringValue(it.jsonPrimitive.content) },
      )
    } ?: emptyList()

  private fun JsonObject.extractImagesFromJson(): List<ChangeItemApi> =
    this[JsonKeys.ITEMS]?.jsonArray?.map { jsonElement ->
      val jsonObject = jsonElement.jsonObject

      val value = jsonObject[ItemJsonKeys.VALUE]?.extractImageValuesFromJson()
      val originalValue = jsonObject[ItemJsonKeys.ORIGINAL_VALUE]?.extractImageValuesFromJson()

      ChangeItemApi(
        id = jsonObject.getValue(ItemJsonKeys.ID).jsonPrimitive.content,
        action = jsonObject.getValue(ItemJsonKeys.ACTION).jsonPrimitive.content,
        time = jsonObject.getValue(ItemJsonKeys.TIME).jsonPrimitive.content,
        iso6391 = jsonObject.getValue(ItemJsonKeys.ISO_639_1).jsonPrimitive.content,
        iso31661 = jsonObject.getValue(ItemJsonKeys.ISO_3166_1).jsonPrimitive.content,
        value = value,
        originalValue = originalValue,
      )
    } ?: emptyList()
}
