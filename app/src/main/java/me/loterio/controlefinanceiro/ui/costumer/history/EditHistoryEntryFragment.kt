package me.loterio.controlefinanceiro.ui.costumer.history

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.android.billingclient.api.*
import kotlinx.android.synthetic.main.costumer_detail.tieAddress
import kotlinx.android.synthetic.main.edit_history_entry.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.loterio.controlefinanceiro.R
import me.loterio.controlefinanceiro.data.model.HistoryEntry
import me.loterio.controlefinanceiro.ui.utils.MyMaskEditText
import timber.log.Timber
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EditHistoryEntryFragment : Fragment(), PurchasesUpdatedListener {
    private lateinit var datePicker: DatePickerDialog
    private val args by navArgs<EditHistoryEntryFragmentArgs>()
    private lateinit var skuDetailsResult: SkuDetailsResult
    private lateinit var billingClient: BillingClient
    var costumerID: String? = null
    private lateinit var viewModel: EditHistoryViewModel
    private lateinit var myCalendar: Calendar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.edit_history_entry, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(EditHistoryViewModel::class.java)
        val observer = Observer<HistoryEntry> {
            val pattern = "dd/MM/yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val currencyFormat = NumberFormat.getCurrencyInstance()

            tieDate.setText(simpleDateFormat.format(it.date))
            tieAddress.setText(it.horas.toString())
            tieFaturado.setText(it.faturado.toString())
            tieAReceber.setText(it.aReceber.toString())
        }

        viewModel.liveData.observe(viewLifecycleOwner, observer)

        btnSaveHistory.setOnClickListener(View.OnClickListener {
            val df = SimpleDateFormat("dd/MM/yyyy")
            val date = df.parse(tieDate.text.toString())
            val horas = (tieAddress.text.toString()).toFloat()
            val faturado = BigDecimal(tieFaturado.text.toString()).toFloat()
            val aReceber = BigDecimal(tieAReceber.text.toString()).toFloat()

            viewModel.writeHistoryEntry(date=date,horas = horas, faturado = faturado, aReceber = aReceber)
            Navigation.findNavController(it).popBackStack()
        })

        tieDate.setOnClickListener(View.OnClickListener {
            datePicker = DatePickerDialog(context!!)
            datePicker.setOnDateSetListener(object : OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var calendar = Calendar.getInstance();
                    calendar.set(year,month,dayOfMonth)
                    var strData = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                    tieDate.setText(strData)
                }

            })
            datePicker.show()
        })

        tieDate.addTextChangedListener(MyMaskEditText(tieDate,"##/##/####"))
    }

    override fun onResume() {
        super.onResume()
            viewModel.findHistoryEntryById(args.costumerId,args.historyEntryId)
    }

    fun pay(){
        billingClient = BillingClient.newBuilder(context!!).setListener(this).enablePendingPurchases().build()
        billingClient.startConnection(object : BillingClientStateListener{
            override fun onBillingServiceDisconnected() {
                Timber.w("onBillingServiceDisconnected")
            }

            override fun onBillingSetupFinished(p0: BillingResult?) {
                Timber.w("onBillingSetupFinished:"+p0?.responseCode)
                (Thread(Runnable {
                    GlobalScope.launch {  querySkuDetails() }
                })).start()
            }

        })


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
