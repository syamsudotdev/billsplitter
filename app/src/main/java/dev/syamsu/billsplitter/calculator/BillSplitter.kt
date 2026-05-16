package dev.syamsu.billsplitter.calculator

import dev.syamsu.billsplitter.model.OrderItem
import dev.syamsu.billsplitter.model.PersonShare

interface BillSplitter {
    fun splitBill(
        items: List<OrderItem>,
        taxPercent: Double,
        tipPercent: Double
    ): List<PersonShare>
}
