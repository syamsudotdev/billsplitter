package dev.syamsu.billsplitter.calculator

import dev.syamsu.billsplitter.model.OrderItem
import dev.syamsu.billsplitter.model.PersonShare

class DefaultBillSplitter : BillSplitter {

    override fun splitBill(
        items: List<OrderItem>,
        taxPercent: Double,
        tipPercent: Double
    ): List<PersonShare> {
        val subtotal = items.sumOf { it.price }
        val taxAmount = subtotal * (taxPercent / 100.0)
        val tipAmount = subtotal * (tipPercent / 100.0)

        return items.groupBy { it.person }.map { (person, personItems) ->
            val itemsTotal = personItems.sumOf { it.price }
            val numberOfPeople = items.groupBy { it.person }.size
            val taxShare = taxAmount / numberOfPeople
            val tipShare = tipAmount / numberOfPeople

            PersonShare(person, itemsTotal, taxShare, tipShare)
        }
    }
}
