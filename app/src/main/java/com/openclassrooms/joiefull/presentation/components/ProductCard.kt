package com.openclassrooms.joiefull.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.openclassrooms.joiefull.domain.model.ClothingItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductCard(
  item: ClothingItem,
  modifier: Modifier = Modifier,
  onClick: (ClothingItem) -> Unit,
  onToggleFavorite: (ClothingItem) -> Unit
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .semantics { contentDescription = "Ouvrir la fiche ${item.name}" },
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    onClick = { onClick(item) }
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(item.imageUrl)
          .crossfade(true)
          .build(),
        contentDescription = "Photo de ${item.name}",
        modifier = Modifier
          .clip(MaterialTheme.shapes.medium)
          .fillMaxWidth()
          .aspectRatio(1f),
        contentScale = ContentScale.Crop
      )
      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = item.name,
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.weight(1f),
          maxLines = 2
        )
        IconButton(
          onClick = { onToggleFavorite(item) },
          modifier = Modifier.size(48.dp)
        ) {
          Icon(
            imageVector = if (item.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (item.isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = item.price.toCurrency(),
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = item.originalPrice.toCurrency(),
          style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textDecoration = TextDecoration.LineThrough
          )
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        RatingBar(rating = item.rating.value)
        Text(
          text = "(${item.rating.count})",
          style = MaterialTheme.typography.labelMedium
        )
      }
    }
  }
}

private fun Double.toCurrency(): String {
  val formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE)
  return formatter.format(this)
}
