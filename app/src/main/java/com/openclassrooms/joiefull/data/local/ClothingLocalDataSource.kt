package com.openclassrooms.joiefull.data.local

/**
 * Simple in-memory storage that simulates persistence for ratings and favorites.
 */
class ClothingLocalDataSource {
  private val ratings: MutableMap<String, RatingEntry> = mutableMapOf()
  private val favorites: MutableSet<String> = mutableSetOf()
  private val shares: MutableMap<String, Int> = mutableMapOf()

  fun saveRating(itemId: String, rating: Float, comment: String) {
    ratings[itemId] = RatingEntry(rating = rating, comment = comment)
  }

  fun getRating(itemId: String): RatingEntry? = ratings[itemId]

  fun getRatingVoteCount(itemId: String): Int = if (ratings.containsKey(itemId)) 1 else 0

  fun getComment(itemId: String): String? = ratings[itemId]?.comment

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
    val comment: String
  )
}
