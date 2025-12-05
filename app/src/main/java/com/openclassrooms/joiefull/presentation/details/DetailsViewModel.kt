package com.openclassrooms.joiefull.presentation.details

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joiefull.di.AppContainer
import com.openclassrooms.joiefull.domain.model.ClothingItem
import com.openclassrooms.joiefull.domain.usecase.GetClothesUseCase
import com.openclassrooms.joiefull.domain.usecase.GetClothingDetailsUseCase
import com.openclassrooms.joiefull.domain.usecase.RegisterShareUseCase
import com.openclassrooms.joiefull.domain.usecase.SaveRatingUseCase
import com.openclassrooms.joiefull.domain.usecase.ToggleFavoriteUseCase
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailsUiState(
  val isLoading: Boolean = true,
  val item: ClothingItem? = null,
  val userRating: Float = 0f,
  val comment: String = "",
  val errorMessage: String? = null
)

class DetailsViewModel(
  private val itemId: String,
  private val getClothesUseCase: GetClothesUseCase,
  private val getClothingDetailsUseCase: GetClothingDetailsUseCase,
  private val saveRatingUseCase: SaveRatingUseCase,
  private val registerShareUseCase: RegisterShareUseCase,
  private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow(DetailsUiState())
  val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

  init {
    loadItem(resetInputs = true)
    observeItemChanges()
  }

  private fun loadItem(resetInputs: Boolean = false) {
    viewModelScope.launch {
      val item = getClothingDetailsUseCase(itemId)
      _uiState.update { state ->
        state.copy(
          isLoading = false,
          item = item,
          userRating = if (resetInputs) 0f else state.userRating,
          comment = if (resetInputs) "" else state.comment
        )
      }
    }
  }

  private fun observeItemChanges() {
    viewModelScope.launch {
      getClothesUseCase().collectLatest { items ->
        val updatedItem = items.firstOrNull { it.id == itemId } ?: return@collectLatest
        _uiState.update { current ->
          current.copy(
            isLoading = false,
            item = updatedItem
          )
        }
      }
    }
  }

  fun updateRating(newValue: Int) {
    _uiState.update { it.copy(userRating = newValue.toFloat()) }
  }

  fun updateComment(newComment: String) {
    _uiState.update { it.copy(comment = newComment) }
  }

  fun saveRating() {
    val current = _uiState.value
    if (current.item == null) return
    viewModelScope.launch {
      saveRatingUseCase(itemId, current.userRating, current.comment)
      loadItem(resetInputs = true)
    }
  }

  fun toggleFavorite() {
    viewModelScope.launch {
      val updated = toggleFavoriteUseCase(itemId)
      _uiState.update { it.copy(item = updated ?: it.item) }
    }
  }

  fun shareItem(context: Context) {
    val item = _uiState.value.item ?: return
    viewModelScope.launch {
      val updated = registerShareUseCase(itemId)
      if (updated != null) {
        _uiState.update { it.copy(item = updated) }
      }
      launchShareIntent(context, updated ?: item)
    }
  }

  companion object {
    fun provideFactory(
      appContainer: AppContainer,
      itemId: String
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DetailsViewModel(
          itemId = itemId,
          getClothesUseCase = appContainer.getClothesUseCase,
          getClothingDetailsUseCase = appContainer.getClothingDetailsUseCase,
          saveRatingUseCase = appContainer.saveRatingUseCase,
          registerShareUseCase = appContainer.registerShareUseCase,
          toggleFavoriteUseCase = appContainer.toggleFavoriteUseCase
        ) as T
      }
    }
  }
}

private fun Double.toCurrencyText(): String {
  val formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE)
  return formatter.format(this)
}

private fun launchShareIntent(context: Context, item: ClothingItem) {
  val shareText = "${item.name} - ${item.price.toCurrencyText()}\n${item.description}"
  val sendIntent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, shareText)
  }
  val chooser = Intent.createChooser(sendIntent, "Partager \"${item.name}\"")
  ContextCompat.startActivity(context, chooser, null)
}
