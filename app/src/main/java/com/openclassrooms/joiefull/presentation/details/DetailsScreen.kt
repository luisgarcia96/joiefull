package com.openclassrooms.joiefull.presentation.details

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.openclassrooms.joiefull.presentation.components.RatingBar
import com.openclassrooms.joiefull.presentation.components.RatingStarColor
import com.openclassrooms.joiefull.presentation.components.RatingStarSize
import com.openclassrooms.joiefull.domain.model.UserReview
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import com.openclassrooms.joiefull.R

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

  if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
    FullscreenDetailsContent(
      uiState = uiState,
      windowSizeClass = windowSizeClass,
      onBack = onBack,
      onShare = { onShare(context) },
      onRatingSelected = onRatingSelected,
      onCommentChanged = onCommentChanged,
      onSaveRating = onSaveRating,
      onToggleFavorite = onToggleFavorite
    )
    return
  }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

  LaunchedEffect(drawerState) {
    snapshotFlow { drawerState.currentValue }
      .filter { it == DrawerValue.Closed }
      .collectLatest { onBack() }
  }

  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    ModalNavigationDrawer(
      drawerState = drawerState,
      gesturesEnabled = true,
      drawerContent = {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
          DetailsDrawerPanel(
            uiState = uiState,
            windowSizeClass = windowSizeClass,
            onBack = onBack,
            onShare = { onShare(context) },
            onRatingSelected = onRatingSelected,
            onCommentChanged = onCommentChanged,
            onSaveRating = onSaveRating,
            onToggleFavorite = onToggleFavorite
          )
        }
      },
      scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f)
    ) {
      CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        )
      }
    }
  }
}

@Composable
private fun DetailsDrawerContent(
  uiState: DetailsUiState,
  windowSizeClass: WindowSizeClass,
  onBack: () -> Unit,
  onShare: () -> Unit,
  onRatingSelected: (Int) -> Unit,
  onCommentChanged: (String) -> Unit,
  onSaveRating: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  val scrollState = rememberScrollState()
  val screenWidth = LocalConfiguration.current.screenWidthDp.dp

  Scaffold(
    topBar = {},
    containerColor = Color.White,
    contentColor = MaterialTheme.colorScheme.onSurface
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      val isWideContent =
        screenWidth >= 1024.dp && windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
      when {
        uiState.isLoading -> {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            CircularProgressIndicator()
          }
        }

        uiState.item != null -> {
          val contentPadding = 20.dp
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(scrollState)
              .padding(horizontal = contentPadding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
          ) {
            DetailsCard(
              modifier = Modifier.fillMaxWidth(),
              imageUrl = uiState.item.imageUrl,
              title = uiState.item.name,
              likes = uiState.item.likes,
              isFavorite = uiState.item.isFavorite,
              onBack = onBack,
              onShare = onShare,
              onToggleFavorite = onToggleFavorite
            )
            DetailsInfo(
              uiState = uiState,
              modifier = Modifier.fillMaxWidth(),
              onRatingSelected = onRatingSelected,
              onCommentChanged = onCommentChanged,
              onSaveRating = onSaveRating
            )
          }
        }
        else -> {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = stringResource(id = R.string.item_not_found),
              style = MaterialTheme.typography.bodyLarge
            )
          }
        }
      }
    }
  }
}

@Composable
private fun DetailsCard(
  modifier: Modifier,
  imageUrl: String,
  title: String,
  likes: Int,
  isFavorite: Boolean,
  onBack: () -> Unit,
  onShare: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  val favoriteActionDescription = if (isFavorite) {
    stringResource(id = R.string.remove_from_favorites)
  } else {
    stringResource(id = R.string.add_to_favorites)
  }
  val favoriteStateDescription = if (isFavorite) {
    stringResource(id = R.string.favorite_state_in)
  } else {
    stringResource(id = R.string.favorite_state_out)
  }
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(24.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(imageUrl)
          .crossfade(true)
          .build(),
        contentDescription = stringResource(id = R.string.product_image_description, title),
        modifier = Modifier
          .fillMaxSize()
          .aspectRatio(0.9f),
        contentScale = ContentScale.Crop
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp)
          .align(Alignment.TopCenter),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        IconPill(
          onClick = onBack,
          icon = Icons.AutoMirrored.Outlined.ArrowBack,
          contentDescription = stringResource(id = R.string.back_to_list)
        )
        IconPill(
          onClick = onShare,
          icon = Icons.Outlined.Share,
          contentDescription = stringResource(id = R.string.share_product, title)
        )
      }

      Surface(
        onClick = onToggleFavorite,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        tonalElevation = 1.dp,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(12.dp)
          .minimumInteractiveComponentSize()
          .semantics {
            role = Role.Button
            contentDescription = favoriteActionDescription
            stateDescription = favoriteStateDescription
          }
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Text(
            text = likes.toString(),
            style = MaterialTheme.typography.titleMedium
          )
        }
      }
    }
  }
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
  val initialPriceContentDescription = stringResource(
    id = R.string.initial_price_content_description,
    item.originalPrice.toCurrency()
  )
  val saveReviewContentDescription = stringResource(id = R.string.save_review_content_description)
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = item.name,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
          .weight(1f)
          .semantics { heading() }
      )
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(
          imageVector = Icons.Rounded.Star,
          contentDescription = stringResource(id = R.string.rating_average),
          tint = RatingStarColor,
          modifier = Modifier.size(RatingStarSize)
        )
        Text(
          text = String.format(Locale.FRANCE, "%.1f", item.rating.value),
          style = MaterialTheme.typography.titleMedium
        )
        if (item.rating.count > 0) {
          Text(
            text = "(${item.rating.count})",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
          )
        }
      }
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(
        text = item.price.toCurrency(),
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
      )
      Text(
        text = item.originalPrice.toCurrency(),
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
          textDecoration = TextDecoration.LineThrough
        ),
        modifier = Modifier.semantics {
          contentDescription = initialPriceContentDescription
        }
      )
    }

    Text(
      text = item.description,
      style = MaterialTheme.typography.bodyLarge
    )

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Icon(
        imageVector = Icons.Outlined.Share,
        contentDescription = stringResource(id = R.string.share_count_label),
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(18.dp)
      )
      Text(
        text = pluralStringResource(
          id = R.plurals.share_count,
          count = item.shareCount,
          item.shareCount
        ),
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
      )
    }

    ReviewSection(
      reviews = item.reviews,
      modifier = Modifier.fillMaxWidth()
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.06f), shape = CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.Person,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      RatingBar(
        rating = uiState.userRating,
        onRatingSelected = onRatingSelected
      )
    }

      OutlinedTextField(
        value = uiState.comment,
        onValueChange = onCommentChanged,
        label = { Text(text = stringResource(id = R.string.comment_label)) },
        placeholder = { Text(text = stringResource(id = R.string.comment_placeholder)) },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
          unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
      )
    }

    Button(
      onClick = onSaveRating,
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .semantics { contentDescription = saveReviewContentDescription },
      shape = RoundedCornerShape(18.dp),
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
      Text(
        text = stringResource(id = R.string.publish_review),
        style = MaterialTheme.typography.titleMedium
      )
    }
  }
}

@Composable
private fun ReviewSection(
  reviews: List<UserReview>,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(
      text = stringResource(id = R.string.reviews_title),
      style = MaterialTheme.typography.titleMedium
    )
    if (reviews.isEmpty()) {
      Text(
        text = stringResource(id = R.string.reviews_empty),
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
      )
    } else {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        reviews.forEach { review ->
          ReviewCard(review = review)
        }
      }
    }
  }
}

@Composable
private fun ReviewCard(
  review: UserReview
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    tonalElevation = 0.dp,
    shadowElevation = 0.dp
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      RatingBar(
        rating = review.rating,
        starSize = 16.dp
      )
      Text(
        text = review.comment.ifBlank { stringResource(id = R.string.review_no_comment) },
        style = MaterialTheme.typography.bodyMedium
      )
      Text(
        text = stringResource(id = R.string.review_note_value, review.rating),
        style = MaterialTheme.typography.labelSmall.copy(
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
      )
    }
  }
}

@Composable
private fun IconPill(
  onClick: () -> Unit,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  contentDescription: String
) {
  Surface(
    shape = CircleShape,
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
    shadowElevation = 6.dp,
    tonalElevation = 1.dp
  ) {
    IconButton(
      onClick = onClick,
      modifier = Modifier
        .minimumInteractiveComponentSize()
        .size(48.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}

private fun Double.toCurrency(): String {
  val formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE)
  return formatter.format(this)
}

@Composable
fun DetailsDrawerPanel(
  uiState: DetailsUiState,
  windowSizeClass: WindowSizeClass,
  onBack: () -> Unit,
  onShare: () -> Unit,
  onRatingSelected: (Int) -> Unit,
  onCommentChanged: (String) -> Unit,
  onSaveRating: () -> Unit,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier
) {
  val drawerPanelDescription = stringResource(id = R.string.details_panel_description)
  Surface(
    modifier = modifier
      .fillMaxHeight()
      .widthIn(min = 0.dp, max = preferredDrawerWidth(windowSizeClass))
      .semantics { contentDescription = drawerPanelDescription },
    tonalElevation = 0.dp,
    shadowElevation = 0.dp,
    shape = RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp),
    color = Color.White
  ) {
    DetailsDrawerContent(
      uiState = uiState,
      windowSizeClass = windowSizeClass,
      onBack = onBack,
      onShare = onShare,
      onRatingSelected = onRatingSelected,
      onCommentChanged = onCommentChanged,
      onSaveRating = onSaveRating,
      onToggleFavorite = onToggleFavorite
    )
  }
}

@Composable
private fun FullscreenDetailsContent(
  uiState: DetailsUiState,
  windowSizeClass: WindowSizeClass,
  onBack: () -> Unit,
  onShare: () -> Unit,
  onRatingSelected: (Int) -> Unit,
  onCommentChanged: (String) -> Unit,
  onSaveRating: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White),
    color = Color.White
  ) {
    DetailsDrawerContent(
      uiState = uiState,
      windowSizeClass = windowSizeClass,
      onBack = onBack,
      onShare = onShare,
      onRatingSelected = onRatingSelected,
      onCommentChanged = onCommentChanged,
      onSaveRating = onSaveRating,
      onToggleFavorite = onToggleFavorite
    )
  }
}

private fun preferredDrawerWidth(windowSizeClass: WindowSizeClass): Dp {
  return when (windowSizeClass.widthSizeClass) {
    WindowWidthSizeClass.Expanded -> 520.dp
    WindowWidthSizeClass.Medium -> 460.dp
    else -> 400.dp
  }
}
