package com.openclassrooms.joiefull.data.remote

import com.openclassrooms.joiefull.data.remote.model.ClothingItemDto
import com.openclassrooms.joiefull.data.remote.model.RatingDto
import com.openclassrooms.joiefull.domain.model.Category

/**
 * Fake remote service returning static data. Later, this can be replaced with a Retrofit service.
 */
class FakeClothingService : ClothingService {
  override suspend fun getClothingItems(): List<ClothingItemDto> = clothingItemsMock
}

private val clothingItemsMock = listOf(
  ClothingItemDto(
    id = "top_1",
    name = "Chemise Lin Aurore",
    description = "Chemise oversize en lin biologique, respirante et douce pour l'été.",
    price = 89.0,
    originalPrice = 129.0,
    imageUrl = "https://picsum.photos/400?random=1",
    category = Category.TOPS,
    rating = RatingDto(value = 4.5f, count = 120),
    likes = 96
  ),
  ClothingItemDto(
    id = "top_2",
    name = "Pull Côtelé Nuit",
    description = "Pull côtelé col montant en laine mérinos certifiée RWS.",
    price = 119.0,
    originalPrice = 139.0,
    imageUrl = "https://picsum.photos/400?random=2",
    category = Category.TOPS,
    rating = RatingDto(value = 4.8f, count = 87),
    likes = 132
  ),
  ClothingItemDto(
    id = "top_3",
    name = "Blouse Brise Marine",
    description = "Blouse fluide manches bouffantes en viscose certifiée Lenzing™ EcoVero™.",
    price = 89.0,
    originalPrice = 119.0,
    imageUrl = "https://picsum.photos/400?random=7",
    category = Category.TOPS,
    rating = RatingDto(value = 4.6f, count = 65),
    likes = 73
  ),
  ClothingItemDto(
    id = "top_4",
    name = "T-shirt Pima Lumière",
    description = "T-shirt en coton Pima 160g, coupe droite, col légèrement ouvert.",
    price = 45.0,
    originalPrice = 55.0,
    imageUrl = "https://picsum.photos/400?random=8",
    category = Category.TOPS,
    rating = RatingDto(value = 4.2f, count = 102),
    likes = 64
  ),
  ClothingItemDto(
    id = "top_5",
    name = "Cardigan Nuit Boréale",
    description = "Cardigan long en maille texturée, poches plaquées, laine recyclée.",
    price = 139.0,
    originalPrice = 169.0,
    imageUrl = "https://picsum.photos/400?random=9",
    category = Category.TOPS,
    rating = RatingDto(value = 4.7f, count = 91),
    likes = 118
  ),
  ClothingItemDto(
    id = "bottom_1",
    name = "Pantalon Voyage",
    description = "Pantalon fluide coupe droite avec ceinture ajustable et poches profondes.",
    price = 99.0,
    originalPrice = 149.0,
    imageUrl = "https://picsum.photos/400?random=3",
    category = Category.BOTTOMS,
    rating = RatingDto(value = 4.3f, count = 64),
    likes = 78
  ),
  ClothingItemDto(
    id = "bottom_2",
    name = "Jupe Mistral",
    description = "Jupe portefeuille midi, tissu satiné infroissable pour vos journées actives.",
    price = 79.0,
    originalPrice = 109.0,
    imageUrl = "https://picsum.photos/400?random=4",
    category = Category.BOTTOMS,
    rating = RatingDto(value = 4.0f, count = 45),
    likes = 54
  ),
  ClothingItemDto(
    id = "bottom_3",
    name = "Jean Selvedge Atelier",
    description = "Jean coupe droite en denim selvedge 12,5 oz, teinture naturelle indigo.",
    price = 149.0,
    originalPrice = 189.0,
    imageUrl = "https://picsum.photos/400?random=10",
    category = Category.BOTTOMS,
    rating = RatingDto(value = 4.5f, count = 133),
    likes = 142
  ),
  ClothingItemDto(
    id = "bottom_4",
    name = "Short Rivage",
    description = "Short taille haute en lin mélangé, ceinture ruban amovible.",
    price = 69.0,
    originalPrice = 89.0,
    imageUrl = "https://picsum.photos/400?random=11",
    category = Category.BOTTOMS,
    rating = RatingDto(value = 4.1f, count = 58),
    likes = 49
  ),
  ClothingItemDto(
    id = "bottom_5",
    name = "Pantalon Studio Tech",
    description = "Pantalon cargo léger, tissu déperlant stretch, ourlets ajustables.",
    price = 129.0,
    originalPrice = 159.0,
    imageUrl = "https://picsum.photos/400?random=12",
    category = Category.BOTTOMS,
    rating = RatingDto(value = 4.4f, count = 77),
    likes = 83
  ),
  ClothingItemDto(
    id = "bag_1",
    name = "Cabas Horizon",
    description = "Cabas vegan convertible en sac à dos, compartiment ordinateur 14 pouces.",
    price = 159.0,
    originalPrice = 189.0,
    imageUrl = "https://picsum.photos/400?random=5",
    category = Category.BAGS,
    rating = RatingDto(value = 4.9f, count = 210),
    likes = 201
  ),
  ClothingItemDto(
    id = "bag_2",
    name = "Mini Sac Mystère",
    description = "Mini sac bandoulière modulable avec chaîne amovible et poches RFID.",
    price = 129.0,
    originalPrice = 149.0,
    imageUrl = "https://picsum.photos/400?random=6",
    category = Category.BAGS,
    rating = RatingDto(value = 4.4f, count = 74),
    likes = 88
  ),
  ClothingItemDto(
    id = "bag_3",
    name = "Banane Éclipse",
    description = "Sac banane grand volume, sangle matelassée, poche cachée anti-RFID.",
    price = 99.0,
    originalPrice = 119.0,
    imageUrl = "https://picsum.photos/400?random=13",
    category = Category.BAGS,
    rating = RatingDto(value = 4.6f, count = 132),
    likes = 157
  ),
  ClothingItemDto(
    id = "bag_4",
    name = "Sac Seau Atelier",
    description = "Sac seau en cuir vegan grainé, bandoulière réglable, base rigide.",
    price = 189.0,
    originalPrice = 219.0,
    imageUrl = "https://picsum.photos/400?random=14",
    category = Category.BAGS,
    rating = RatingDto(value = 4.3f, count = 51),
    likes = 69
  ),
  ClothingItemDto(
    id = "bag_5",
    name = "Tote Pliable Aurora",
    description = "Tote pliable imperméable, se range dans sa poche, renforts cousus.",
    price = 59.0,
    originalPrice = 69.0,
    imageUrl = "https://picsum.photos/400?random=15",
    category = Category.BAGS,
    rating = RatingDto(value = 4.2f, count = 88),
    likes = 94
  )
)
