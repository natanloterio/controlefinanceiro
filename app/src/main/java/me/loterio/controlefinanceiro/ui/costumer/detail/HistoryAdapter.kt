package me.loterio.controlefinanceiro.ui.costumer.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.loterio.controlefinanceiro.R
import me.loterio.controlefinanceiro.data.model.HistoryEntry

class HistoryAdapter(
    val historyEntries: List<HistoryEntry>,
    val context: Context
) : RecyclerView.Adapter<HistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
       return HistoryViewHolder(LayoutInflater.from(context).inflate( R.layout.costumer_history_item,parent,false))
    }

    override fun getItemCount(): Int {
        return historyEntries.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyEntries.get(position))
    }
}


