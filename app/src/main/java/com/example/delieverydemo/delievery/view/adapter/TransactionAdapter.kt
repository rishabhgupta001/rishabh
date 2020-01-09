package com.example.delieverydemo.delievery.view.adapter

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.delieverydemo.R
import com.example.delieverydemo.databinding.ItemLayoutTransactionBinding
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants
import com.example.delieverydemo.utils.Utils

class TransactionAdapter(val list: ArrayList<DeliveryResponseModel>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding: ItemLayoutTransactionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_layout_transaction,
            parent,
            false
        )

        return TransactionViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    inner class TransactionViewHolder(val binding: ItemLayoutTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                itemView.isEnabled = false
                Handler().postDelayed({
                    itemView.isEnabled = true
                    val arguments = Bundle()
                    it.findNavController().navigate(R.id.action_delivery_detail, arguments)
                }, 100)
            }
        }

        fun bindItem(data: DeliveryResponseModel) {
            binding.data = data
            Utils.setImage(binding.productImageView, data.goodsPicture)
            val price = data.deliveryFee.removePrefix("$").toFloat() + data.surcharge.removePrefix("$").toFloat()
            binding.priceTxtView.text = price.toString()
        }
    }
}