package com.example.okhttpissues.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class NetworkChecker {
    companion object {
        fun isOnline(context: Context): Boolean {
            var result: Boolean = false
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connMgr.run {
                connMgr.getNetworkCapabilities(connMgr.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            }
            Log.d("NetworkAvailable", result.toString())
            return result
        }
    }
}