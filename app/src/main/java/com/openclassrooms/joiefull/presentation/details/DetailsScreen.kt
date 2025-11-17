package com.openclassrooms.joiefull.presentation.details

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.openclassrooms.joiefull.presentation.components.AppTopBar
import com.openclassrooms.joiefull.presentation.components.RatingBar
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailsScreen(
  uiState: DetailsUiState,
  windowSizeClass: WindowSizeClass,
  onBack: () -> Unit,
  onShare: (Context) -> Unit,
  onRatingSelected: (Int) -> Unit,
  onCommentChanged: (String) -> Unit,
  onSaveRating: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  val context = LocalContext.current
  val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

  Scaffold(
    topBar = {
      AppTopBar(
        title = uiState.item?.name ?: "Détails",
        onBack = onBack,
        onShare = { onShare(context) },
        onFavorite = { onToggleFavorite() },
        isFavorite = uiState.item?.isFavorite == true
      )
    }
  ) { innerPadding ->
    when {
      uiState.isLoading -> {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          CircularProgressIndicator()
        }
      }

      uiState.item != null -> {
        if (isExpanded) {
          Row(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
              .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
          ) {
            DetailsImage(
              modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
              imageUrl = uiState.item.imageUrl,
              title = uiState.item.name
            )
            DetailsInfo(
              uiState = uiState,
              modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
              onRatingSelected = onRatingSelected,
              onCommentChanged = onCommentChanged,
              onSaveRating = onSaveRating
            )
          }
        } else {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
              .verticalScroll(rememberScrollState())
          ) {
            DetailsImage(
              modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
              imageUrl = uiState.item.imageUrl,
              title = uiState.item.name
            )
            DetailsInfo(
              uiState = uiState,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
              onRatingSelected = onRatingSelected,
              onCommentChanged = onCommentChanged,
              onSaveRating = onSaveRating
            )
          }
        }
      }
    }
  }
}

@Composable
private fun DetailsImage(
  modifier: Modifier,
  imageUrl: String,
  title: String
) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(imageUrl)
      .crossfade(true)
      .build(),
    contentDescription = "Visuel de $title",
    modifier = modifier,
    contentScale = ContentScale.Crop
  )
}

@Composable
private fun DetailsInfo(
  uiState: DetailsUiState,
  modifier: Modifier = Modifier,
  onRatingSelected: (Int) -> Unit,
  onCommentChanged: (String) -> Unit,
  onSaveRating: () -> Unit
) {
  val item = uiState.item ?: return
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = item.name,
      style = MaterialTheme.typography.headlineMedium,
      modifier = Modifier.semantics { heading() }
    )
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = item.price.toCurrency(),
        style = MaterialTheme.typography.titleMedium
      )
      Text(
        text = item.originalPrice.toCurrency(),
        style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough),
        modifier = Modifier.semantics {
          contentDescription = "Prix initial ${item.originalPrice} euros"
        }
      )
    }
    RatingBar(
      rating = uiState.userRating,
      onRatingSelected = onRatingSelected
    )
    Text(
      text = item.description,
      style = MaterialTheme.typography.bodyLarge
    )
    OutlinedTextField(
      value = uiState.comment,
      onValueChange = onCommentChanged,
      label = { Text("Commentaire") },
      modifier = Modifier.fillMaxWidth(),
      minLines = 3
    )
    Button(
      onClick = onSaveRating,
      modifier = Modifier
        .fillMaxWidth()
        .semantics { contentDescription = "Enregistrer mon avis" }
    ) {
      Text(text = "Publier mon avis")
    }
  }
}

private fun Double.toCurrency(): String {
  val formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE)
  return formatter.format(this)
}
