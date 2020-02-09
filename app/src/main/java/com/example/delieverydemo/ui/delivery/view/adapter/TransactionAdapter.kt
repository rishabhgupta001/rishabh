package com.example.delieverydemo.ui.delivery.view.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.delieverydemo.R
import com.example.delieverydemo.data.network.NetworkState
import com.example.delieverydemo.ui.delivery.model.DeliveryResponseModel

class TransactionAdapter(private val retryCallback: () -> Unit) :
    PagedListAdapter<DeliveryResponseModel, RecyclerView.ViewHolder>(diff) {
    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_layout_transaction -> (holder as DeliveryPostViewHolder).bind(
                getItem(
                    position
                )
            )
            R.layout.item_network_state -> (holder as NetworkSateItemViewHolder).bind(networkState)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_layout_transaction -> DeliveryPostViewHolder.create(parent)
            R.layout.item_network_state -> NetworkSateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalStateException("unknow view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_layout_transaction
        }
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<DeliveryResponseModel>() {
            override fun areItemsTheSame(
                oldItem: DeliveryResponseModel,
                newItem: DeliveryResponseModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DeliveryResponseModel,
                newItem: DeliveryResponseModel
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.isFavourite == newItem.isFavourite
            }

        }
    }

    private fun hasExtraRow(): Boolean =
        networkState != null && networkState != NetworkState.SUCCESS


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val previousExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val newExtraRow = hasExtraRow()

        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

}