<<<<<<<< HEAD:core/navigation/src/main/kotlin/com/divinelink/core/navigation/arguments/DetailsNavArguments.kt
package com.divinelink.core.navigation.arguments
========
package com.divinelink.feature.details.media.ui
>>>>>>>> a88d13c6 (feat: repackage details):feature/details/src/main/kotlin/com/divinelink/feature/details/media/ui/DetailsNavArguments.kt

/**
 * Information required when launching the movie details screen.
 */
data class DetailsNavArguments(
  val id: Int,
  val mediaType: String,
  val isFavorite: Boolean?,
)
