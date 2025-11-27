package com.openclassrooms.joiefull.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.presentation.home.HomeTextStyles
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductCard(
  item: ClothingItem,
  modifier: Modifier = Modifier,
  textStyles: HomeTextStyles,
  onClick: (ClothingItem) -> Unit,
  onToggleFavorite: (ClothingItem) -> Unit
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .semantics { contentDescription = "Ouvrir la fiche ${item.name}" },
    colors = CardDefaults.cardColors(containerColor = Color.White),
    onClick = { onClick(item) }
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Box(
        modifier = Modifier
          .clip(MaterialTheme.shapes.medium)
          .fillMaxWidth()
          .aspectRatio(1f)
      ) {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(item.imageUrl)
            .crossfade(true)
            .build(),
          contentDescription = "Photo de ${item.name}",
          modifier = Modifier.matchParentSize(),
          contentScale = ContentScale.Crop
        )
        Surface(
          onClick = { onToggleFavorite(item) },
          shape = RoundedCornerShape(24.dp),
          color = Color.White,
          shadowElevation = 4.dp,
          tonalElevation = 0.dp,
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp)
        ) {
          Row(
            modifier = Modifier
              .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = if (item.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = if (item.isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = item.likes.toString(),
              style = textStyles.productMeta
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = item.name,
          style = textStyles.productTitle,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        RatingRow(
          item = item,
          textStyles = textStyles
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = item.price.toCurrency(),
          style = textStyles.productPrice
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
          text = item.originalPrice.toCurrency(),
          style = textStyles.productPrice.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textDecoration = TextDecoration.LineThrough
          ),
          textAlign = TextAlign.End
        )
      }
    }
  }
}

@Composable
private fun RatingRow(
  item: ClothingItem,
  textStyles: HomeTextStyles
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Icon(
      imageVector = Icons.Rounded.Star,
      contentDescription = "Note de ${item.name}",
      tint = Color(0xFFFFA726),
      modifier = Modifier.size(20.dp)
    )
    Text(
      text = String.format(Locale.FRANCE, "%.1f", item.rating.value),
      style = textStyles.productPrice
    )
    if (item.rating.count > 0) {
      Text(
        text = "(${item.rating.count})",
        style = textStyles.productMeta.copy(
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
      )
    }
  }
}

private fun Double.toCurrency(): String {
  val formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE)
  val hasCents = this % 1 != 0.0
  formatter.maximumFractionDigits = if (hasCents) 2 else 0
  formatter.minimumFractionDigits = if (hasCents) 2 else 0
  return formatter.format(this)
}
