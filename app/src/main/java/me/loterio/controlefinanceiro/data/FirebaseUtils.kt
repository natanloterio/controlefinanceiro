package me.loterio.controlefinanceiro.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import me.loterio.controlefinanceiro.data.model.Costumer
import me.loterio.controlefinanceiro.data.model.HistoryEntry
import java.math.BigDecimal
import java.util.*

object FirebaseUtils {

    val USERS = "users"
    val COSTUMERS = "costumers"
    val HISTORY = "history"
    val db = FirebaseDatabase.getInstance()
    val costumerRef = db.getReference(COSTUMERS)
    val historyRef = db.getReference(HISTORY)

    fun writeNewCostumer(costumerId: String, name: String, address: String, phone: String){
        var costumer = Costumer(id=costumerId, name=name, address=address, phone=phone)
        costumerRef.child(costumerId).setValue(costumer)
    }
    fun writeHistoryEntry(historyEntryId: String, costumerId: String,date: Date, horas: Float, faturado: Float, aReceber: Float){
        var historyEntry = HistoryEntry(historyEntryId,costumerId,date,horas,faturado,aReceber)
        historyRef.child(historyEntryId).setValue(historyEntry)
    }

    fun updateCostumer(id: String, name: String, address: String, phone: String){
        var costumer = Costumer(id=id, name=name, address=address, phone=phone)
        costumerRef.child(id).setValue(costumer)
    }

    fun getCostumer(id: String, listener: ValueEventListener) {
        costumerRef.orderByKey().equalTo(id).addValueEventListener(listener)
    }
    fun getHistoryEntry(id: String, listener: ValueEventListener) {
        historyRef.orderByKey().equalTo(id).addValueEventListener(listener)
    }

    fun getCostumerHistoryByUser(id: String, listener: ValueEventListener) {
        historyRef.orderByChild("costumerId").equalTo(id).addValueEventListener(listener)
    }

    fun listCostumers(listener: ValueEventListener){
        costumerRef.orderByKey().addListenerForSingleValueEvent(listener)
    }


}

