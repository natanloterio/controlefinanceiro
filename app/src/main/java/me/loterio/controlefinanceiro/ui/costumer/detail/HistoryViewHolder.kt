package me.loterio.controlefinanceiro.ui.costumer.detail

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import me.loterio.controlefinanceiro.R
import me.loterio.controlefinanceiro.data.model.HistoryEntry
import java.text.NumberFormat
import java.text.SimpleDateFormat

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var mHistoryEntry: HistoryEntry

    fun bind(historyEntry: HistoryEntry){
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val currencyFormat = NumberFormat.getCurrencyInstance()

        mHistoryEntry = historyEntry
        itemView.findViewById<TextView>(R.id.tvDate).setText(simpleDateFormat.format(historyEntry.date))
        itemView.findViewById<TextView>(R.id.tvTotalHoras).setText(historyEntry.horas.toString())
        itemView.findViewById<TextView>(R.id.tvFaturado).setText(currencyFormat.format(historyEntry.faturado))
        itemView.findViewById<TextView>(R.id.tvAReceber).setText(currencyFormat.format(historyEntry.aReceber))

        itemView.setOnClickListener(View.OnClickListener {
            val action = CostumerDertailFragmentDirections.actionCostumerDetailFragmentToEditHistoryEntryFragment(costumerId = mHistoryEntry.costumerId, historyEntryId = mHistoryEntry.historyEntryId)
            Navigation.findNavController(it).navigate(action)
        })
    }

}