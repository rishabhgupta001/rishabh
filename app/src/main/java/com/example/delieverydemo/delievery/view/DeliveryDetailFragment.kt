package com.example.delieverydemo.delievery.view


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.delieverydemo.R
import com.example.delieverydemo.databinding.FragmentDeliveryDetailBinding
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Utils

/**
 *
​
 * Purpose – Class gives details about delivery of product
​
 * @author ​Rishabh Gupta
​
 * Created on January 9, 2020
​
 * Modified on January 9, 2020
 *
 * */
class DeliveryDetailFragment : Fragment() {
    private var itemData: DeliveryResponseModel? = null
    private lateinit var binding: FragmentDeliveryDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_detail, container, false)

        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    /**
     * initalization
     */
    @SuppressLint("SetTextI18n")
    private fun init() {

        arguments?.let {
            itemData = DeliveryDetailFragmentArgs.fromBundle(it).deliveryResponseModel
            binding.data = itemData
            Utils.setImage(binding.productImageView, itemData?.goodsPicture!!)
            val price =
                itemData?.deliveryFee?.removePrefix("$")?.toFloat()!! + itemData?.surcharge?.removePrefix(
                    "$"
                )?.toFloat()!!
            binding.feeTextView.text = "$${price}"
        }
    }

}
