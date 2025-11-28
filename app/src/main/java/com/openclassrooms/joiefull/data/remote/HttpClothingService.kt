package com.openclassrooms.joiefull.data.remote

import com.openclassrooms.joiefull.data.remote.model.ClothingItemDto
import com.openclassrooms.joiefull.data.remote.model.RatingDto
import com.openclassrooms.joiefull.domain.model.Category
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

private const val CLOTHES_ENDPOINT =
  "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/api/clothes.json"

/**
 * Simple HTTP implementation of [ClothingService] that hits the provided JSON endpoint.
 * No Retrofit dependency is needed for this single GET call.
 */
class HttpClothingService(
  private val endpoint: String = CLOTHES_ENDPOINT
) : ClothingService {

  override suspend fun getClothingItems(): List<ClothingItemDto> = withContext(Dispatchers.IO) {
    val connection = URL(endpoint).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connectTimeout = 5_000
    connection.readTimeout = 5_000

    try {
      if (connection.responseCode != HttpURLConnection.HTTP_OK) {
        return@withContext emptyList()
      }
      val payload = connection.inputStream.bufferedReader().use { it.readText() }
      parseClothingItems(payload)
    } catch (_: Exception) {
      emptyList()
    } finally {
      connection.disconnect()
    }
  }

  private fun parseClothingItems(rawJson: String): List<ClothingItemDto> {
    val jsonArray = JSONArray(rawJson)
    return buildList {
      for (index in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(index)
        add(jsonObject.toClothingItemDto())
      }
    }
  }

  private fun JSONObject.toClothingItemDto(): ClothingItemDto {
    val picture = optJSONObject("picture")
    val nameValue = optString("name")
    val priceValue = optDouble("price", 0.0)
    val originalPriceValue = optDouble("original_price", priceValue).orFallback(priceValue)
    val categoryRaw = optString("category")

    return ClothingItemDto(
      id = optString("id", optInt("id").toString()),
      name = nameValue,
      description = picture?.optString("description").orEmpty().ifBlank { nameValue },
      price = priceValue,
      originalPrice = originalPriceValue,
      imageUrl = picture?.optString("url").orEmpty(),
      category = Category.fromApiValue(categoryRaw),
      rating = RatingDto(value = 0f, count = 0),
      likes = optInt("likes", 0),
      shares = optInt("shares", 0)
    )
  }
}

private fun Double.orFallback(fallback: Double): Double =
  if (this.isNaN()) fallback else this
