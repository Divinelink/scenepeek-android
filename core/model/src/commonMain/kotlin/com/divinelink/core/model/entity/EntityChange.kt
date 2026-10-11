package com.divinelink.core.model.entity

/**
 * Used to observe Added or Removed entities from a database table.
 */
sealed interface EntityChange {
  data class Added(val ids: Set<Long>) : EntityChange
  data class Removed(val ids: Set<Long>) : EntityChange
}
