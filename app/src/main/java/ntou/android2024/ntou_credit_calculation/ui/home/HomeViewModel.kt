package ntou.android2024.ntou_credit_calculation.ui.home

import androidx.lifecycle.ViewModel

data class DiceUiState(
    val firstDieValue: Int? = null,
    val secondDieValue: Int? = null,
    val numberOfRolls: Int = 0,
)

class HomeViewModel : ViewModel() {



}