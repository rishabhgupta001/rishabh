package com.example.delieverydemo.ui.delivery.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.delieverydemo.data.repositories.DeliveryRepository
import com.example.delieverydemo.ui.delivery.viewmodel.DeliveryViewModel

@Suppress("UNCHECKED_CAST")
class DeliveryViewModelFactory(private val repositry: DeliveryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DeliveryViewModel(repositry) as T

    }
}