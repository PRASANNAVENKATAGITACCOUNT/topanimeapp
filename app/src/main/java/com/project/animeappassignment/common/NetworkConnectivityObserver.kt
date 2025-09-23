package com.project.animeappassignment.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserver(context: Context) {
   private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observe(): Flow<NetworkStatus> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                    trySend(NetworkStatus.Available)
                }
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                
                if (hasInternet && isValidated) {
                    trySend(NetworkStatus.Available)
                } else {
                    trySend(NetworkStatus.Unavailable)
                }
            }

            override fun onLost(network: Network) {
                val currentStatus = getCurrentNetworkStatus()
                trySend(currentStatus)
            }

            override fun onUnavailable() {
                trySend(NetworkStatus.Unavailable)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, networkCallback)
            
            val initialStatus = getCurrentNetworkStatus()
            trySend(initialStatus)
        } catch (e: Exception) {
            trySend(NetworkStatus.Unavailable)
        }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.distinctUntilChanged()

    private fun getCurrentNetworkStatus(): NetworkStatus {
        return try {
            val network = connectivityManager.activeNetwork ?: return NetworkStatus.Unavailable
            val capabilities = connectivityManager.getNetworkCapabilities(network) 
                ?: return NetworkStatus.Unavailable
            
            when {
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> {
                    NetworkStatus.Available
                }
                else -> NetworkStatus.Unavailable
            }
        } catch (e: Exception) {
            NetworkStatus.Unavailable
        }
    }
}

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Lost : NetworkStatus()
    object Unavailable : NetworkStatus()
}
