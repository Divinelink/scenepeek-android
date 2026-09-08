package com.divinelink.core.model.track

enum class ShowStatus(val value: String) {
  WATCHING("watching"),
  COMPLETED("completed"),
  PLAN_TO_WATCH("plan_to_watch"),
  ON_HOLD("on_hold"),
  DROPPED("dropped"),
  ;

  companion object {
    fun from(value: String): ShowStatus = ShowStatus.entries.find {
      it.value == value
    } ?: WATCHING
  }
}
