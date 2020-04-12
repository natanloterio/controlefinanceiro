package me.loterio.controlefinanceiro.data.model

import java.math.BigDecimal
import java.util.*

data class HistoryEntry(
    var historyEntryId: String = "",
    var costumerId: String = "",
    var date: Date,
    var horas: Float,
    var  faturado: Float,
    var  aReceber: Float
){
    constructor(): this("","",Date(), 0f,0f,0f)
}

