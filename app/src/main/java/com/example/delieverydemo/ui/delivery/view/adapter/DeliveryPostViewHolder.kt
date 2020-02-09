package com.example.delieverydemo.ui.delivery.view.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.delieverydemo.R
import com.example.delieverydemo.databinding.ItemLayoutTransactionBinding
import com.example.delieverydemo.ui.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.ui.delivery.view.DeliveryFragmentDirections
import com.example.delieverydemo.utils.Utils


/**
 * A RecyclerView ViewHolder that displays a Delivery post.
 */
class DeliveryPostViewHolder(val binding: ItemLayoutTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var data: DeliveryResponseModel? = null

    init {
        itemView.setOnClickListener {
            itemView.isEnabled = false
            Handler().postDelayed({
                itemView.isEnabled = true

                data?.let {
                    val action = DeliveryFragmentDirections.actionDeliveryDetail()
                    action.deliveryResponseModel = it
                    Navigation.findNavController(binding.root).navigate(action)
                }

            }, 100)
        }
    }

    fun bind(data: DeliveryResponseModel?) {
        this.data = data
        binding.data = data
        Utils.setImage(binding.productImageView, data?.goodsPicture!!)
    }

    companion object {
        fun create(parent: ViewGroup): DeliveryPostViewHolder {
            //item_network_state layout inflated
            val binding =
                DataBindingUtil.inflate<ItemLayoutTransactionBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_layout_transaction,
                    parent,
                    false
                )
            return DeliveryPostViewHolder(binding)
        }
    }
}