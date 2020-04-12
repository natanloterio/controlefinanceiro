package me.loterio.controlefinanceiro.ui.costumer.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import me.loterio.controlefinanceiro.data.FirebaseUtils
import me.loterio.controlefinanceiro.data.model.Costumer
import timber.log.Timber
import java.lang.Exception

class CostumerListViewModel() : ViewModel() {
    val listener = object :
        FirebaseListener {
        override fun onLoadCostumer(costumer: List<Costumer>) {
            liveData.postValue(costumer)
        }
    }

    fun listAll() {
        FirebaseUtils.listCostumers(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapShot: DataSnapshot) {
                val result = snapShot.children.map{
                    it -> it.getValue<Costumer>(Costumer::class.java)
                }
                liveData.postValue(result as List<Costumer>?)
            }

        })
    }

    val liveData: MutableLiveData<List<Costumer>> by lazy {
        MutableLiveData<List<Costumer>>()
    }

    interface FirebaseListener {
        fun onLoadCostumer(costumer:List<Costumer>);
    }
}
