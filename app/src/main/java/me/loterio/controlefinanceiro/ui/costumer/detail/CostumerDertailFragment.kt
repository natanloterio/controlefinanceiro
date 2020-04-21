package me.loterio.controlefinanceiro.ui.costumer.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.*
import kotlinx.android.synthetic.main.costumer_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.loterio.controlefinanceiro.R
import me.loterio.controlefinanceiro.data.model.Costumer
import me.loterio.controlefinanceiro.data.model.HistoryEntry
import java.util.*
import kotlin.collections.ArrayList


class CostumerDertailFragment : Fragment(), PurchasesUpdatedListener {
    private val args by navArgs<CostumerDertailFragmentArgs>()
    private lateinit var skuDetailsResult: SkuDetailsResult
    private lateinit var billingClient: BillingClient
    var costumerID: String? = null
    private lateinit var viewModel: CostumerDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.costumer_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(CostumerDetailViewModel::class.java)
        val costumerObserver = Observer<Costumer> {

            tieDate.setText(it.name)
            tieAddress.setText(it.address)
            tiePhone.setText(it.phone)
        }

        val historyEntriesObserver = Observer<List<HistoryEntry>> {
            rvCostumerHistory.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvCostumerHistory.adapter = makeAdapter(it)
        }


        viewModel.costumerDetails.observe(viewLifecycleOwner, costumerObserver)
        viewModel.historyEntries.observe(viewLifecycleOwner, historyEntriesObserver)

        fabNewHistory.setOnClickListener(View.OnClickListener {
            val action = CostumerDertailFragmentDirections.actionCostumerDetailFragmentToEditHistoryEntryFragment(
                costumerId = viewModel.mCostumerID,
                historyEntryId = UUID.randomUUID().toString())

            Navigation.findNavController(it).navigate(action)
        })

    }

    private fun saveData() {
        val name = tieDate.text.toString()
        val address = tieAddress.text.toString()
        val phone = tiePhone.text.toString()
        viewModel.writeCostumer(name, address, phone)
    }

    override fun onResume() {
        super.onResume()
        viewModel.findCostumerDetailsById(args.costumerId)
        viewModel.findCostumerHistoryById(args.costumerId)
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun makeAdapter(it: List<HistoryEntry>): HistoryAdapter {
        return HistoryAdapter(it, activity!!.applicationContext)
    }

    override fun onPurchasesUpdated(p0: BillingResult?, p1: MutableList<Purchase>?) {
        TODO("Not yet implemented")
    }

    suspend fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add("me.loterio.produto")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }
        if( skuDetailsResult.skuDetailsList != null ){
            val list = skuDetailsResult.skuDetailsList
            if(!list!!.isEmpty()) {
                val detail = list!!.first();
                if (detail != null) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(detail)
                        .build()
                    val responseCode = billingClient.launchBillingFlow(activity, flowParams)
                }
            }
        }

    }

}
