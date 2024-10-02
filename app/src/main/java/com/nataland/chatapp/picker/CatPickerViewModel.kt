package com.nataland.chatapp.picker

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CatPickerState(
    val selectedCat: Cat? = null,
    val cats: List<Cat> = CatInfo.allCats
)

@HiltViewModel
class CatPickerViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(CatPickerState())
    val uiState = _uiState.asStateFlow()

    fun setCat(cat: Cat) {
        _uiState.value = _uiState.value.copy(selectedCat = cat)
    }
}
