package com.example.delieverydemo.ui.delivery.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.delieverydemo.R
import com.example.delieverydemo.data.network.NetworkState
import com.example.delieverydemo.data.network.StatusCode
import com.example.delieverydemo.databinding.NetworkItemBinding
import kotlinx.android.synthetic.main.item_network_state.view.*


/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkSateItemViewHolder(
    val binding: NetworkItemBinding,
    private val retryCallback: () -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.retry_button.setOnClickListener { retryCallback() }
    }


    fun bind(networkState: NetworkState?) {
        itemView.progressBar.visibility = isVisible(networkState?.statusCode == StatusCode.START)
        /*itemView.retry_button.visibility = isVisible(networkState?.statusCode == StatusCode.ERROR)
        itemView.error_msg.visibility = isVisible(!networkState?.msg.isNullOrEmpty())
        itemView.error_msg.text = networkState?.msg*/

    }

    fun isVisible(constraint: Boolean): Int = if (constraint) View.VISIBLE else View.GONE


    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkSateItemViewHolder {
            //item_network_state layout inflated
            val binding =
                DataBindingUtil.inflate<NetworkItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_network_state,
                    parent,
                    false
                )
            return NetworkSateItemViewHolder(binding, retryCallback)
        }
    }
}