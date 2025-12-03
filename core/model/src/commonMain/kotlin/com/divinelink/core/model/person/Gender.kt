package com.divinelink.core.model.person

import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.gender_female
import com.divinelink.core.model.resources.gender_male
import com.divinelink.core.model.resources.gender_non_binary
import com.divinelink.core.model.resources.gender_not_set
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class Gender(
  val value: Int,
  val stringRes: StringResource,
) {
  NOT_SET(value = 0, stringRes = Res.string.gender_not_set),
  FEMALE(value = 1, stringRes = Res.string.gender_female),
  MALE(value = 2, stringRes = Res.string.gender_male),
  NON_BINARY(value = 3, stringRes = Res.string.gender_non_binary),
  ;

  companion object {
    fun from(value: Int?) = entries.find { it.value == value } ?: NOT_SET
  }
}
