package com.openclassrooms.joiefull.domain.model

/**
 * Aggregates the rating value and total number of votes for a clothing item.
 */
data class Rating(
  val value: Float,
  val count: Int
)
