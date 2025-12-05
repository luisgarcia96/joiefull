package com.openclassrooms.joiefull.domain.model

/**
 * Represents a user review submitted from the device.
 */
data class UserReview(
  val rating: Float,
  val comment: String,
  val createdAt: Long
)
