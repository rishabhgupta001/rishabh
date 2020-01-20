package com.example.delieverydemo.delievery.view.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.delieverydemo.R
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.api.StatusCode
import com.example.delieverydemo.databinding.ItemLayoutTransactionBinding
import com.example.delieverydemo.databinding.NetworkItemBinding
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.delievery.view.DeliveryFragmentDirections
import com.example.delieverydemo.utils.Pref.TYPE_ITEM
import com.example.delieverydemo.utils.Pref.TYPE_PROGRESS
import com.example.delieverydemo.utils.Utils

class TransactionAdapter :
    PagedListAdapter<DeliveryResponseModel, RecyclerView.ViewHolder>(diff) {
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)

        if (viewType == TYPE_PROGRESS) {
            //item_network_state layout inflated
            val binding = NetworkItemBinding.inflate(inflater, parent, false)
            return NetworkStateItemViewHolder(binding)

        } else {
            //notifications_item_layout layout inflated
            val binding = DataBindingUtil.inflate<ItemLayoutTransactionBinding>(
                inflater,
                R.layout.item_layout_transaction,
                parent,
                false
            )
            return TransactionViewHolder(binding)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TransactionViewHolder) {
            holder.bindItem(this.getItem(position)!!)

        } else if (holder is NetworkStateItemViewHolder) {
            holder.bind(networkState)
        }
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<DeliveryResponseModel>() {
            override fun areItemsTheSame(
                p0: DeliveryResponseModel,
                p1: DeliveryResponseModel
            ): Boolean {
                return p0.id == p1.id
            }

            override fun areContentsTheSame(
                p0: DeliveryResponseModel,
                p1: DeliveryResponseModel
            ): Boolean {
                return p0.id == p1.id
            }

        }
    }

    inner class TransactionViewHolder(val binding: ItemLayoutTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                itemView.isEnabled = false
                Handler().postDelayed({
                    itemView.isEnabled = true

                    val action = DeliveryFragmentDirections.actionDeliveryDetail()
                    action.deliveryResponseModel = getItem(adapterPosition)
                    Navigation.findNavController(it).navigate(action)
                }, 100)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindItem(
            data: DeliveryResponseModel
        ) {
            binding.data = data
            Utils.setImage(binding.productImageView, data.goodsPicture)
        }
    }


    class NetworkStateItemViewHolder(var binding: NetworkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState.statusCode == StatusCode.START) {
                binding.progressBarPaging.setVisibility(View.VISIBLE)
            } else {
                //Status SUCCESS
                binding.progressBarPaging.setVisibility(View.GONE)
            }

            if (networkState != null && networkState.statusCode == StatusCode.ERROR) {
                binding.errorMsg.setVisibility(View.VISIBLE)
                binding.errorMsg.setText(networkState.msg)
            } else {
                binding.errorMsg.setVisibility(View.GONE)
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        if (networkState != null && networkState !== NetworkState.SUCCESS)
            return true
        else
            return false
    }

    override fun getItemViewType(position: Int): Int {
        if (hasExtraRow() && position == itemCount - 1) {
            return TYPE_PROGRESS
        } else {
            return TYPE_ITEM
        }
    }

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