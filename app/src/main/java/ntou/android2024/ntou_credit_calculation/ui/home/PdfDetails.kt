package ntou.android2024.ntou_credit_calculation.ui.home

data class PdfDetails(
    val Name: String,
    val studentId: String,
    val totalCredit: Int,
    val subjectList: List<subject>
)
