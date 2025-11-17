package com.openclassrooms.joiefull.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
  title: String,
  modifier: Modifier = Modifier,
  onBack: (() -> Unit)? = null,
  onShare: (() -> Unit)? = null,
  onFavorite: (() -> Unit)? = null,
  isFavorite: Boolean = false
) {
  TopAppBar(
    title = { Text(text = title) },
    modifier = modifier,
    navigationIcon = {
      if (onBack != null) {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = "Revenir en arrière"
          )
        }
      }
    },
    actions = {
      if (onShare != null) {
        IconButton(onClick = onShare) {
          Icon(
            imageVector = Icons.Outlined.Share,
            contentDescription = "Partager la fiche"
          )
        }
      }
      if (onFavorite != null) {
        IconButton(onClick = onFavorite) {
          Icon(
            imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris"
          )
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors()
  )
}
