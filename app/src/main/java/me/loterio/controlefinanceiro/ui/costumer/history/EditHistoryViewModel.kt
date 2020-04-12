package me.loterio.controlefinanceiro.ui.costumer.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import me.loterio.controlefinanceiro.data.FirebaseUtils
import me.loterio.controlefinanceiro.data.model.HistoryEntry
import timber.log.Timber
import java.math.BigDecimal
import java.util.*

class EditHistoryViewModel() : ViewModel() {
    lateinit var mHistoryEntryId: String
    lateinit var mCostumerId: String
    val listener = object :
        FirebaseListener {
        override fun onLoadHistoryEntry(historyEntry: HistoryEntry) {
            liveData.postValue(historyEntry)
        }
    }

    fun findHistoryEntryById(costumerId:String,historyEntryId: String) {
        mCostumerId = costumerId
        mHistoryEntryId = historyEntryId
        FirebaseUtils.getHistoryEntry(historyEntryId, object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Timber.d("Erro ao getCostumer viewmodel")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue() != null) {
                    val updatedCostumer: HistoryEntry =
                        snapshot.children!!.first().getValue<HistoryEntry>(HistoryEntry::class.java)!!
                    if (updatedCostumer != null) {
                        listener.onLoadHistoryEntry(updatedCostumer)
                    }
                }else{
                    val updatedCostumer = HistoryEntry(costumerId=mCostumerId,historyEntryId = mHistoryEntryId,date=Date(),horas=0f,faturado=0f,aReceber=0f)
                    listener.onLoadHistoryEntry(updatedCostumer)
                }
            }

        });
    }



    fun writeHistoryEntry(date: Date, horas: Float, faturado: Float, aReceber: Float) {
        FirebaseUtils.writeHistoryEntry(mHistoryEntryId,mCostumerId, date,horas, faturado, aReceber)
    }

    val liveData: MutableLiveData<HistoryEntry> by lazy {
        MutableLiveData<HistoryEntry>()
    }

    interface FirebaseListener {
        fun onLoadHistoryEntry(costumer:HistoryEntry);
    }
}
