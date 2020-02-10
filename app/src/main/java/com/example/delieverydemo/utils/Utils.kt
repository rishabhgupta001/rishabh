package com.example.delieverydemo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.delieverydemo.R
import com.google.android.material.snackbar.Snackbar

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
     * Method to set Image with placeholder
     */
    fun setImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView).load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.placeholder_products)
            .into(imageView)
    }

    /**
     * Method to show msg to user using Snackbar
     */
    fun showSnackBar(
        view: View,
        msg: String,
        actionName: String,
        onClickListener: View.OnClickListener
    ) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction(actionName) {

                onClickListener.onClick(it)
            }
        }.show()


    }
}