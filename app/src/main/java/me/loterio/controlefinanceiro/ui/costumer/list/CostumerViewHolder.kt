package me.loterio.controlefinanceiro.ui.costumer.list

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.costumer_list_item.view.*
import me.loterio.controlefinanceiro.R
import me.loterio.controlefinanceiro.data.model.Costumer

class CostumerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var mCostumer: Costumer

    fun bind(costumer: Costumer){
        mCostumer = costumer
        itemView.findViewById<TextView>(R.id.tvCostumerName).setText(costumer.name)
        itemView.setOnClickListener(View.OnClickListener {
            val action = CostumerListFragmentDirections.actionCostumerListFragmentToCostumerDetailFragment(costumerId = mCostumer.id)
            Navigation.findNavController(it).navigate(action)
        })
    }

}