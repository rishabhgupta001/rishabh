package com.example.delieverydemo.delievery.view


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.delieverydemo.R
import com.example.delieverydemo.databinding.FragmentDeliveryDetailBinding
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Utils
import kotlinx.android.synthetic.main.fragment_delivery_detail.*

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
class DeliveryDetailFragment : Fragment(), View.OnClickListener {
    private var itemData: DeliveryResponseModel? = null
    private lateinit var binding: FragmentDeliveryDetailBinding

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.add_fav_btn -> {
                add_fav_btn.isEnabled = false
                Handler().postDelayed({
                    add_fav_btn.isEnabled = true

                    if (itemData != null) {
                        itemData?.isFavourite = !itemData?.isFavourite!!
                        if (itemData?.isFavourite!!) {
                            delive_text_view.text = getString(R.string.text_add_to_fav)
                            fav_img_view.setImageResource(R.drawable.img_fav_black)
                        } else {
                            delive_text_view.text = getString(R.string.text_remove_fav)
                            fav_img_view.setImageResource(R.drawable.img_fav_black_border)
                        }
                    }
                }, 100)
            }
        }
    }

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
        add_fav_btn.setOnClickListener(this)

        arguments?.let {
            itemData = DeliveryDetailFragmentArgs.fromBundle(it).deliveryResponseModel
            binding.data = itemData
            Utils.setImage(binding.productImageView, itemData?.goodsPicture!!)
        }
    }

}
