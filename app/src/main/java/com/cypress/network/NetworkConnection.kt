package com.cypress.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context) : LiveData<Boolean>() {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {

        super.onActive()

        updateNetworkConnection()

        when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {

                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())

            }

            else -> {

                context.registerReceiver(

                    networkReceiver,

                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

                )

            }

        }

    }

    override fun onInactive() {

        super.onInactive()

        try {

            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback())

        } catch (e: Exception) {

            e.printStackTrace()

        }

    }

    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {

        networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onLost(network: Network) {

                super.onLost(network)

                postValue(false)

            }

            override fun onAvailable(network: Network) {

                super.onAvailable(network)

                postValue(true)

            }

        }

        return networkConnectionCallback

    }

    private fun updateNetworkConnection() {

        postValue(isNetworkAvailable())

    }

    private val networkReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            updateNetworkConnection()

        }

    }

    fun isNetworkAvailable(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val nw = connectivityManager.activeNetwork ?: return false

            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

            return when {

                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true

                else -> false

            }

        } else {

            return connectivityManager.activeNetworkInfo?.isConnected ?: false

        }

    }

}