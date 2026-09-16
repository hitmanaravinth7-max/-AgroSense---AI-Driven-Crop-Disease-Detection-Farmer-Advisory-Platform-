package com.example.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AgroSenseData
import com.example.model.AnalysisResult
import com.example.model.Crop
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AgroSenseUiState(
    val selectedCropId: String = "tomato",
    val selectedSymptoms: Set<String> = emptySet(),
    val imageUri: Uri? = null,
    val isAnalyzing: Boolean = false,
    val hasAnalyzed: Boolean = false,
    val results: List<AnalysisResult> = emptyList(),
    val errorMessage: String? = null
)

class AgroSenseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AgroSenseUiState())
    val uiState: StateFlow<AgroSenseUiState> = _uiState.asStateFlow()

    val availableCrops: List<Crop> = AgroSenseData.crops
    val availableSymptoms: List<String> = AgroSenseData.symptoms

    fun selectCrop(cropId: String) {
        _uiState.value = _uiState.value.copy(
            selectedCropId = cropId,
            errorMessage = null
        )
        // If already analyzed, refresh analysis with new crop
        if (_uiState.value.hasAnalyzed && _uiState.value.selectedSymptoms.isNotEmpty()) {
            val updated = AgroSenseData.analyze(cropId, _uiState.value.selectedSymptoms)
            _uiState.value = _uiState.value.copy(results = updated)
        }
    }

    fun toggleSymptom(symptom: String) {
        val current = _uiState.value.selectedSymptoms.toMutableSet()
        if (current.contains(symptom)) {
            current.remove(symptom)
        } else {
            current.add(symptom)
        }
        _uiState.value = _uiState.value.copy(
            selectedSymptoms = current,
            errorMessage = null
        )
    }

    fun clearSymptoms() {
        _uiState.value = _uiState.value.copy(
            selectedSymptoms = emptySet(),
            results = emptyList(),
            hasAnalyzed = false,
            errorMessage = null
        )
    }

    fun setImageUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    fun analyze() {
        val state = _uiState.value
        if (state.selectedSymptoms.isEmpty()) {
            _uiState.value = state.copy(
                errorMessage = "Please select at least one observed symptom to analyze.",
                hasAnalyzed = false,
                results = emptyList()
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAnalyzing = true, errorMessage = null)
            delay(250) // Smooth tactile feel for analysis
            val results = AgroSenseData.analyze(state.selectedCropId, state.selectedSymptoms)
            _uiState.value = _uiState.value.copy(
                isAnalyzing = false,
                hasAnalyzed = true,
                results = results,
                errorMessage = null
            )
        }
    }

    fun resetAll() {
        _uiState.value = AgroSenseUiState()
    }
}
