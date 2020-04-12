package me.loterio.controlefinanceiro.ui.costumer.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import me.loterio.controlefinanceiro.data.FirebaseUtils
import me.loterio.controlefinanceiro.data.model.Costumer
import me.loterio.controlefinanceiro.data.model.HistoryEntry
import timber.log.Timber

class CostumerDetailViewModel() : ViewModel() {
    lateinit var mCostumerID: String
    val listener = object :
        FirebaseListener {
        override fun onLoadCostumer(costumer: Costumer) {
            costumerDetails.postValue(costumer)
        }

        override fun onLoadHistoryEntries(entries: List<HistoryEntry>) {
            historyEntries.postValue(entries)
        }
    }

    fun findCostumerDetailsById(costumerID: String) {
        mCostumerID = costumerID
        FirebaseUtils.getCostumer(costumerID, object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Timber.d("Erro ao getCostumer viewmodel")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue() != null) {
                    val updatedCostumer: Costumer =
                        snapshot.children!!.first().getValue<Costumer>(Costumer::class.java)!!
                    if (updatedCostumer != null) {
                        listener.onLoadCostumer(updatedCostumer)
                    }
                }
            }

        });
    }

    fun findCostumerHistoryById(costumerID: String) {
        mCostumerID = costumerID
        ServerValue.TIMESTAMP
        FirebaseUtils.getCostumerHistoryByUser(costumerID, object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Timber.d("Erro ao getCostumer viewmodel")
            }

            override fun onDataChange(snapShot: DataSnapshot) {
                if(snapShot.getValue() != null) {
                    val result = snapShot.children.map{
                            it -> it.getValue<HistoryEntry>(HistoryEntry::class.java)
                    }
                    historyEntries.postValue(result as List<HistoryEntry>?)
                }
            }

        });
    }

    fun writeCostumer( name: String, address: String, phone: String) {
        FirebaseUtils.writeNewCostumer(mCostumerID, name,address, phone)
    }

    val costumerDetails: MutableLiveData<Costumer> by lazy {
        MutableLiveData<Costumer>()
    }

    val historyEntries: MutableLiveData<List<HistoryEntry>> by lazy {
        MutableLiveData<List<HistoryEntry>>()
    }

    interface FirebaseListener {
        fun onLoadCostumer(costumer: Costumer);
        fun onLoadHistoryEntries(historyEntries:List<HistoryEntry>);
    }
}
