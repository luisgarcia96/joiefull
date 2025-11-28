package com.openclassrooms.joiefull.domain.model

/**
 * Represents the high level clothing categories displayed on the home screen.
 */
enum class Category(
  val displayName: String,
  val apiValue: String
) {
  TOPS(displayName = "Hauts", apiValue = "TOPS"),
  BOTTOMS(displayName = "Bas", apiValue = "BOTTOMS"),
  ACCESSORIES(displayName = "Accessoires", apiValue = "ACCESSORIES"),
  SHOES(displayName = "Chaussures", apiValue = "SHOES");

  companion object {
    fun fromDisplayName(label: String): Category =
      entries.firstOrNull { it.displayName.equals(label, ignoreCase = true) } ?: TOPS

    fun fromApiValue(value: String): Category =
      entries.firstOrNull { it.apiValue.equals(value, ignoreCase = true) } ?: TOPS
  }
}
