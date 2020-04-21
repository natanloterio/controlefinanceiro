package me.loterio.controlefinanceiro.ui.costumer.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.costumer_list.*
import kotlinx.android.synthetic.main.main_activity.*
import me.loterio.controlefinanceiro.R
import me.loterio.controlefinanceiro.data.model.Costumer
import java.util.*


class CostumerListFragment : Fragment() {

    companion object {
        fun newInstance() = CostumerListFragment()
    }

    var costumerID: String? = null
    private lateinit var viewModel: CostumerListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.costumer_list, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CostumerListViewModel::class.java)
        val observer = Observer<List<Costumer>> {
            rvCostumerList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvCostumerList.adapter = makeAdapter(it)
        }


        viewModel.liveData.observe(viewLifecycleOwner, observer)

        fabAddCostumer.setOnClickListener(View.OnClickListener {
            val action = CostumerListFragmentDirections.actionCostumerListFragmentToCostumerDetailFragment(costumerId = UUID.randomUUID().toString())
            Navigation.findNavController(it).navigate(action)
        })
    }



    override fun onResume() {
        super.onResume()
        viewModel.listAll()
    }

    private fun makeAdapter(it: List<Costumer>): CostumerAdapter {
        return CostumerAdapter(it, activity!!.applicationContext)
    }

}
