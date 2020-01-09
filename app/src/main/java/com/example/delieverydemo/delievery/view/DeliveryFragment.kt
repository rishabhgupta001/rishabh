package com.example.delieverydemo.delievery.view


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delieverydemo.MainActivity
import com.example.delieverydemo.R
import com.example.delieverydemo.api.StatusListener
import com.example.delieverydemo.databinding.FragmentDeliveryBinding
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.delievery.view.adapter.TransactionAdapter
import com.example.delieverydemo.delievery.viewmodel.DeliveryViewModel
import com.example.delieverydemo.utils.hide
import com.example.delieverydemo.utils.show
import com.example.delieverydemo.utils.toastShort
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_delivery.*

/**
 *
​
 * Purpose – Class contains each item code for delivery
​
 * @author ​Rishabh Gupta
​
 * Created on January 8, 2020
​
 * Modified on January 9, 2020
 *
 * */
class DeliveryFragment : Fragment(), StatusListener {
    private lateinit var binding: FragmentDeliveryBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private var list: ArrayList<DeliveryResponseModel>? = null

    val viewModel: DeliveryViewModel by lazy {
        ViewModelProviders.of(this).get(DeliveryViewModel::class.java)
    }

    //lifecycle method OnCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery, container, false);

        return binding.getRoot()
    }

    //lifecycle method onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }


    /**
     * initalization
     */
    private fun init() {
        initializeAdapter()
        observeDeliveryResponse()
        viewModel.getDeliveryList()
        viewModel.statusListener = this
    }

    /**
     * Method to setUp Adapter
     */
    @SuppressLint("WrongConstant")
    private fun initializeAdapter() {
        deliveries_recycler_view.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        list = ArrayList<DeliveryResponseModel>()
        transactionAdapter = TransactionAdapter(list!!)
        deliveries_recycler_view.adapter = transactionAdapter
    }

    /**
     * Method to observe Delivery Response
     */
    private fun observeDeliveryResponse() {
        viewModel.deliveryResponseData.removeObservers(this)
        viewModel.deliveryResponseData.observe(this, Observer { success ->
            if (success != null)
                setUpData(success)

            if (viewModel.deliveryResponseData.value != null)
                viewModel.deliveryResponseData.value = null
        })
    }

    /**
     * Method to setUp Response data in View
     */
    private fun setUpData(data: ArrayList<DeliveryResponseModel>) {
        list?.clear()
        list?.addAll(data)
        transactionAdapter.notifyDataSetChanged()
    }

    override fun onStarted() {
        (activity as MainActivity).progress_bar.show()
    }

    override fun onSuccess(T: Any) {
        (activity as MainActivity).progress_bar.hide()
    }

    override fun onFailure(message: String) {
        (activity as MainActivity).progress_bar.hide()
        context?.toastShort(message)
    }

}

