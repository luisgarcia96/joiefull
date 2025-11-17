package com.openclassrooms.joiefull.domain.model

/**
 * Domain model used across the UI and use cases.
 */
data class ClothingItem(
  val id: String,
  val name: String,
  val description: String,
  val price: Double,
  val originalPrice: Double,
  val imageUrl: String,
  val category: Category,
  val rating: Rating,
  val isFavorite: Boolean,
  val userComment: String? = null
)
