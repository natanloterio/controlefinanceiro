package me.loterio.controlefinanceiro.ui.costumer.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.loterio.controlefinanceiro.R
import me.loterio.controlefinanceiro.data.model.Costumer

class CostumerAdapter(
    val costumer: List<Costumer>,
    val context: Context
) : RecyclerView.Adapter<CostumerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostumerViewHolder {
       return CostumerViewHolder(LayoutInflater.from(context).inflate(R.layout.costumer_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return costumer.size
    }

    override fun onBindViewHolder(holder: CostumerViewHolder, position: Int) {
        holder.bind(costumer.get(position))
    }
}


