package com.openclassrooms.joiefull.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
  rating: Float,
  modifier: Modifier = Modifier,
  starSize: Dp = 18.dp,
  onRatingSelected: ((Int) -> Unit)? = null
) {
  Row(
    modifier = modifier.semantics {
      contentDescription = "Note $rating sur 5"
    },
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    repeat(5) { index ->
      val starNumber = index + 1
      val icon = when {
        rating >= starNumber -> Icons.Filled.Star
        rating in (starNumber - 1f + 0.25f)..<starNumber.toFloat() -> Icons.Rounded.StarHalf
        else -> Icons.Outlined.Star
      }
      val isSelected = rating >= starNumber || icon == Icons.Rounded.StarHalf
      val tint = if (isSelected) {
        MaterialTheme.colorScheme.primary
      } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
      }
      if (onRatingSelected != null) {
        IconButton(
          onClick = { onRatingSelected.invoke(starNumber) },
          modifier = Modifier.size(maxOf(starSize, 48.dp))
        ) {
          Icon(
            imageVector = icon,
            contentDescription = "Sélectionner $starNumber étoile(s)",
            tint = tint
          )
        }
      } else {
        Icon(
          imageVector = icon,
          contentDescription = null,
          modifier = Modifier.size(starSize),
          tint = tint
        )
      }
    }
  }
}
