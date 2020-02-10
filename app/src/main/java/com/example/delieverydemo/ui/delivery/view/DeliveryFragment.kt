package com.example.delieverydemo.ui.delivery.view


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delieverydemo.R
import com.example.delieverydemo.data.network.NetworkState
import com.example.delieverydemo.data.network.StatusCode
import com.example.delieverydemo.databinding.FragmentDeliveryBinding
import com.example.delieverydemo.ui.delivery.view.adapter.TransactionAdapter
import com.example.delieverydemo.ui.delivery.viewmodel.DeliveryViewModel
import com.example.delieverydemo.ui.delivery.viewmodelfactory.DeliveryViewModelFactory
import com.example.delieverydemo.utils.Constants.DEFAULT_SUBDELIVERY
import com.example.delieverydemo.utils.Constants.KEY_SUBDELIVERY
import com.example.delieverydemo.utils.Utils
import kotlinx.android.synthetic.main.fragment_delivery.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 *
​
 * Purpose – Class contains each item code for delivery
​
 * @author ​Rishabh Gupta
​
 * Created on January 8, 2020
​
 * Modified on January 13, 2020
 *
 * */
class DeliveryFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: DeliveryViewModelFactory by instance()

    private lateinit var binding: FragmentDeliveryBinding
    private lateinit var transactionAdapter: TransactionAdapter

    private val viewModel: DeliveryViewModel by lazy {
        ViewModelProviders.of(this, factory).get(DeliveryViewModel::class.java)
    }

    //lifecycle method OnCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery, container, false)

        return binding.root
    }

    //lifecycle method onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(savedInstanceState)
    }


    /**
     * initial setUp
     */
    private fun init(savedInstanceState: Bundle?) {
        setUpRecyclerViewData()
        observeNetworkState()
        initSwipeRefresh()
        val subDelivery = savedInstanceState?.getString(KEY_SUBDELIVERY) ?: DEFAULT_SUBDELIVERY
        viewModel.showSubdelivery(subDelivery)
    }

    /**
     * initalise recyclerview
     */
    private fun setUpRecyclerViewData() {
        swipeRefresh.setColorSchemeColors(Color.RED, Color.RED, Color.RED, Color.RED)
        deliveries_recycler_view.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        transactionAdapter = TransactionAdapter() { viewModel.retry() }

        //get Page List
        viewModel.post.observe(this, Observer { pagedList ->
            //submitList is another special PagedListAdapter method that
            // feeds a PagedList into the adapter and automatically starts the loading process.

            //The PagedListAdapter is now receiving a PagedList, which,
            // in turn, is calling into the DataSource to generate new items.
            transactionAdapter.submitList(pagedList)
        })
    }

    /**
     * Method to observe status of data loading from web services
     */
    private fun observeNetworkState() {
        //get the Network state (on hitting api respone success,error an all..)
        viewModel.networkState.observe(this,
            Observer<NetworkState> { networkState ->
                if (networkState.statusCode == StatusCode.ERROR) {
                    Utils.showSnackBar(root_layout, networkState.msg!!, getString(R.string.retry), object : View.OnClickListener {
                        override fun onClick(view: View?) {
                            viewModel.retry()
                        }
                    })
                }
                //show loader inside adapter row via Network Status
                transactionAdapter.setNetworkState(networkState!!)
            })

        deliveries_recycler_view.adapter = transactionAdapter
    }

    /**
     * Method to handle pull to refresh functionality
     */
    private fun initSwipeRefresh() {
        viewModel.refreshState.observe(this, Observer {
            swipeRefresh.isRefreshing = it == NetworkState.START
        })

        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SUBDELIVERY, viewModel.getCurrentSubDelivery())
    }
}

