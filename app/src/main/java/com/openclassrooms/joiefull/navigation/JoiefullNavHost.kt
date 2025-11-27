package com.openclassrooms.joiefull.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.openclassrooms.joiefull.di.AppContainer
import com.openclassrooms.joiefull.presentation.details.DetailsDrawerPanel
import com.openclassrooms.joiefull.presentation.details.DetailsScreen
import com.openclassrooms.joiefull.presentation.details.DetailsUiState
import com.openclassrooms.joiefull.presentation.details.DetailsViewModel
import com.openclassrooms.joiefull.presentation.home.HomeScreen
import com.openclassrooms.joiefull.presentation.home.HomeViewModel
import com.openclassrooms.joiefull.presentation.components.CARD_WIDTH_FACTOR

@Composable
fun JoiefullNavHost(
  windowSizeClass: WindowSizeClass,
  appContainer: AppContainer
) {
  if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
    TabletHomeWithDetails(windowSizeClass = windowSizeClass, appContainer = appContainer)
  } else {
    PhoneNavHost(windowSizeClass = windowSizeClass, appContainer = appContainer)
  }
}

@Composable
private fun PhoneNavHost(
  windowSizeClass: WindowSizeClass,
  appContainer: AppContainer
) {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = NavRoutes.HOME) {
    composable(NavRoutes.HOME) {
      val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(appContainer)
      )
      val state by homeViewModel.uiState.collectAsStateWithLifecycle()
      HomeScreen(
        uiState = state,
        windowSizeClass = windowSizeClass,
        onProductClick = { navController.navigate(NavRoutes.detailsRoute(it.id)) },
        onToggleFavorite = { homeViewModel.toggleFavorite(it) }
      )
    }
    composable(
      route = "${NavRoutes.DETAILS}/{${NavRoutes.DETAILS_ARG_ID}}",
      arguments = listOf(
        navArgument(NavRoutes.DETAILS_ARG_ID) { type = NavType.StringType }
      )
    ) { entry ->
      val itemId = entry.arguments?.getString(NavRoutes.DETAILS_ARG_ID) ?: return@composable
      val detailsViewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModel.provideFactory(
          appContainer = appContainer,
          itemId = itemId
        )
      )
      val state by detailsViewModel.uiState.collectAsStateWithLifecycle()
      DetailsScreen(
        uiState = state,
        windowSizeClass = windowSizeClass,
        onBack = { navController.popBackStack() },
        onShare = { context ->
          detailsViewModel.shareItem(context)
        },
        onRatingSelected = { rating -> detailsViewModel.updateRating(rating) },
        onCommentChanged = { detailsViewModel.updateComment(it) },
        onSaveRating = { detailsViewModel.saveRating() },
        onToggleFavorite = { detailsViewModel.toggleFavorite() }
      )
    }
  }
}

@Composable
private fun TabletHomeWithDetails(
  windowSizeClass: WindowSizeClass,
  appContainer: AppContainer
) {
  val homeViewModel: HomeViewModel = viewModel(
    factory = HomeViewModel.provideFactory(appContainer)
  )
  val homeState by homeViewModel.uiState.collectAsStateWithLifecycle()
  var selectedItemId by remember { mutableStateOf<String?>(null) }
  val detailsVisibleState = remember { androidx.compose.animation.core.MutableTransitionState(false) }
  val context = LocalContext.current
  val configuration = LocalConfiguration.current
  val density = LocalDensity.current
  val screenWidth = configuration.screenWidthDp.dp
  val columns = 4
  val horizontalPadding = 16.dp
  val spacing = 12.dp
  val baseAvailableWidth =
    (screenWidth - horizontalPadding * 2 - spacing * (columns - 1)).coerceAtLeast(0.dp)
  val cardWidthOverride = (baseAvailableWidth / columns * CARD_WIDTH_FACTOR).coerceAtLeast(0.dp)
  val detailsPanelWidth = detailsPanelWidthFor(windowSizeClass)
  val animatedPanelWidth by animateDpAsState(
    targetValue = if (detailsVisibleState.targetState) detailsPanelWidth else 0.dp,
    animationSpec = spring(
      stiffness = Spring.StiffnessLow,
      dampingRatio = 0.85f
    ),
    label = "detailsPanelWidth"
  )
  val animatedHomeWidth = (screenWidth - animatedPanelWidth).coerceAtLeast(0.dp)
  val panelProgress = if (detailsPanelWidth == 0.dp) 0f else (animatedPanelWidth / detailsPanelWidth).coerceIn(0f, 1f)

  val detailsViewModel: DetailsViewModel? = selectedItemId?.let { itemId ->
    viewModel(
      key = "details-$itemId",
      factory = DetailsViewModel.provideFactory(
        appContainer = appContainer,
        itemId = itemId
      )
    )
  }
  val detailsState by detailsViewModel?.uiState?.collectAsStateWithLifecycle()
    ?: remember { mutableStateOf(DetailsUiState(isLoading = true)) }

  val shouldShowDetails = detailsVisibleState.currentState || detailsVisibleState.targetState

  BackHandler(enabled = shouldShowDetails) {
    detailsVisibleState.targetState = false
  }

  LaunchedEffect(detailsVisibleState.currentState, detailsVisibleState.targetState) {
    if (!detailsVisibleState.currentState && !detailsVisibleState.targetState) {
      selectedItemId = null
    }
  }

  Row(modifier = Modifier.fillMaxSize()) {
    Box(
      modifier = Modifier
        .width(animatedHomeWidth)
        .fillMaxHeight()
    ) {
      HomeScreen(
        uiState = homeState,
        windowSizeClass = windowSizeClass,
        onProductClick = {
          selectedItemId = it.id
          detailsVisibleState.targetState = true
        },
        onToggleFavorite = { homeViewModel.toggleFavorite(it) },
        cardWidthOverride = cardWidthOverride
      )
    }

    if (detailsVisibleState.currentState || detailsVisibleState.targetState) {
      Box(
        modifier = Modifier
          .fillMaxHeight()
          .width(animatedPanelWidth)
          .graphicsLayer {
            alpha = panelProgress
            translationX = (1f - panelProgress) * with(density) { detailsPanelWidth.toPx() }
          }
      ) {
        DetailsDrawerPanel(
          uiState = detailsState,
          windowSizeClass = windowSizeClass,
          onBack = { detailsVisibleState.targetState = false },
          onShare = { detailsViewModel?.shareItem(context) },
          onRatingSelected = { rating -> detailsViewModel?.updateRating(rating) },
          onCommentChanged = { detailsViewModel?.updateComment(it) },
          onSaveRating = { detailsViewModel?.saveRating() },
          onToggleFavorite = { detailsViewModel?.toggleFavorite() }
        )
      }
    }
  }
}

private fun detailsPanelWidthFor(windowSizeClass: WindowSizeClass): Dp {
  return when (windowSizeClass.widthSizeClass) {
    WindowWidthSizeClass.Expanded -> 520.dp
    WindowWidthSizeClass.Medium -> 460.dp
    else -> 400.dp
  }
}
