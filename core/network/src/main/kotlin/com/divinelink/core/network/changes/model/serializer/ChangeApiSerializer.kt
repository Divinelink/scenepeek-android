package com.divinelink.core.network.changes.model.serializer

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
    serialName = ChangeApi::class.java.name,
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

    val changeKey = ChangeKeyType.from(key)

    return when (changeKey) {
      ChangeKeyType.TRANSLATIONS,
      ChangeKeyType.BIOGRAPHY,
      ChangeKeyType.NAME,
      -> extractStringFromJson()
      ChangeKeyType.IMAGES -> extractImagesFromJson()
      ChangeKeyType.VIDEOS -> extractFromJson<VideosValue>()
      ChangeKeyType.CAST -> extractFromJson<CastValue>()
      ChangeKeyType.PLOT_KEYWORDS -> extractFromJson<PlotKeywordsValue>()
      ChangeKeyType.RELEASE_DATES -> extractFromJson<ReleaseDatesValue>()
      ChangeKeyType.ALTERNATIVE_TITLES -> extractFromJson<AlternativeTitlesValue>()
      ChangeKeyType.TAGLINE -> extractFromJson<TaglineValue>()
      else -> {
        // TODO Send error log
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
