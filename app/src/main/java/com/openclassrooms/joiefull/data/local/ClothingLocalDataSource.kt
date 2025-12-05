package com.openclassrooms.joiefull.data.local

/**
 * Simple in-memory storage that simulates persistence for ratings and favorites.
 */
class ClothingLocalDataSource {
  private val ratings: MutableMap<String, MutableList<RatingEntry>> = mutableMapOf()
  private val favorites: MutableSet<String> = mutableSetOf()
  private val shares: MutableMap<String, Int> = mutableMapOf()

  fun saveRating(itemId: String, rating: Float, comment: String) {
    val entries = ratings.getOrPut(itemId) { mutableListOf() }
    entries.add(
      RatingEntry(
        rating = rating,
        comment = comment,
        createdAt = System.currentTimeMillis()
      )
    )
  }

  fun getRatings(itemId: String): List<RatingEntry> = ratings[itemId].orEmpty()

  fun getRatingVoteCount(itemId: String): Int = ratings[itemId]?.size ?: 0

  fun toggleFavorite(itemId: String): Boolean {
    return if (favorites.contains(itemId)) {
      favorites.remove(itemId)
      false
    } else {
      favorites.add(itemId)
      true
    }
  }

  fun isFavorite(itemId: String): Boolean = favorites.contains(itemId)

  fun incrementShare(itemId: String): Int {
    val updated = (shares[itemId] ?: 0) + 1
    shares[itemId] = updated
    return updated
  }

  fun getShareCount(itemId: String): Int = shares[itemId] ?: 0

  data class RatingEntry(
    val rating: Float,
    val comment: String,
    val createdAt: Long
  )
}
