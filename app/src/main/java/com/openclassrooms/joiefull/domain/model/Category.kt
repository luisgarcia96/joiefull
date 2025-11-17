package com.openclassrooms.joiefull.domain.model

/**
 * Represents the high level clothing categories displayed on the home screen.
 */
enum class Category(val displayName: String) {
  TOPS("Hauts"),
  BOTTOMS("Bas"),
  BAGS("Sacs");

  companion object {
    fun fromDisplayName(label: String): Category =
      entries.firstOrNull { it.displayName.equals(label, ignoreCase = true) } ?: TOPS
  }
}
