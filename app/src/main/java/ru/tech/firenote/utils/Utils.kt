package ru.tech.firenote.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.graphics.ColorUtils

object Utils {

    fun Int.blend(ratio: Float = 0.2f, with: Int = 0x000000) =
        ColorUtils.blendARGB(this, with, ratio)

    fun Context.isOnline(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            when {
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
                else -> false
            }
        } else @Suppress("DEPRECATION") {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}