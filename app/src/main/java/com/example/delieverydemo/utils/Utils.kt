package com.example.delieverydemo.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.delieverydemo.R

object Utils {
    /***
     * Method to show Fade In Animation on Texts
     */
    fun showFadeInAnimOnText(context: Context, textView: TextView, msg: String) {
        val animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        textView.visibility = View.VISIBLE
        textView.text = msg
        textView.startAnimation(animFadeIn)
    }

    /**
     * returns true if Internet is connected
     */
    fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * method used to hide the softKeyboard
     */
    fun hideKeyPad(activity: Activity) {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    /**
     * Method to set Image with placeholder
     */
    fun setImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView).load(imageUrl)
            .placeholder(R.drawable.placeholder_products)
            .error(R.drawable.ic_launcher_background)
            .into(imageView)
    }

    /**
     * set Image
     */
    fun setImage(context: Context, imagePath: String, imageView: ImageView) {
        Glide.with(context).load(imagePath).into(imageView)
    }
}