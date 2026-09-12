package com.divinelink.core.model.track

enum class UserShowStatus(val value: String) {
  WATCHING("watching"),
  COMPLETED("completed"),
  PLAN_TO_WATCH("plan_to_watch"),
  ON_HOLD("on_hold"),
  DROPPED("dropped"),
  ;

  companion object {
    fun from(value: String?): UserShowStatus? = UserShowStatus.entries.find {
      it.value == value
    }
  }
}
