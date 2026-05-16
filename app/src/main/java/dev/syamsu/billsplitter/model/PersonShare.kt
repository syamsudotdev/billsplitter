package dev.syamsu.billsplitter.model

data class PersonShare(
    val person: String,
    val itemsTotal: Double,
    val taxShare: Double,
    val tipShare: Double
) {
    val total: Double get() = itemsTotal + taxShare + tipShare
}
