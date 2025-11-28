package com.openclassrooms.joiefull.data.remote.model

import com.openclassrooms.joiefull.domain.model.Category

data class ClothingItemDto(
  val id: String,
  val name: String,
  val description: String,
  val price: Double,
  val originalPrice: Double,
  val imageUrl: String,
  val category: Category,
  val rating: RatingDto = RatingDto(value = 0f, count = 0),
  val likes: Int,
  val shares: Int = 0
)
